package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.BancoCuenta;
import util.ConexionDB;

public class BancoCuentaDAO {

    private static final Logger LOGGER = Logger.getLogger(BancoCuentaDAO.class.getName());

    public Integer registrarBancoCuenta(BancoCuenta bancoCuenta) {

        String sql = "INSERT INTO banco_cuenta (nro_de_cuenta, moneda, descripcion, id_banco, estado) VALUES(?, ?, ?, ?,?)";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, bancoCuenta.getNro_cuenta());
            stmt.setString(2, bancoCuenta.getMoneda());
            stmt.setString(3, bancoCuenta.getDescripcion());
            stmt.setLong(4, bancoCuenta.getId_banco());
            stmt.setInt(5, bancoCuenta.getEstado());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al insertar bancoCuenta: ", ex.getMessage());
        }
        return null;
    }

    public boolean actualizarBancoCuenta(BancoCuenta bancoCuenta) {
        String sql = "UPDATE banco_cuenta SET nro_de_cuenta = ?, moneda = ?, descripcion = ?, id_banco = ?, estado = ? WHERE id_cuenta=?";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bancoCuenta.getNro_cuenta());
            stmt.setString(2, bancoCuenta.getMoneda());
            stmt.setString(3, bancoCuenta.getDescripcion());
            stmt.setLong(4, bancoCuenta.getId_banco());
            stmt.setInt(5, bancoCuenta.getEstado());
            stmt.setLong(6, bancoCuenta.getId_cuenta());

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizar bancoCuenta: ", ex.getMessage());
        }
        return Boolean.FALSE;
    }

    public BancoCuenta obteneBancoCuentaPorId(Long idBancoCuenta) {
        String sql = "SELECT * FROM banco_cuenta WHERE id_cuenta = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idBancoCuenta);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearResultSet(rs);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obteneBancoCuentaPorId: ", ex.getMessage());
        }

        return null;
    }

    public boolean eliminarBancoCuenta(long idBancoCuenta) {
        if (idBancoCuenta <= 0) {
            return false;
        }
        String sql = "DELETE FROM banco_cuenta WHERE id_cuenta = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setLong(1, idBancoCuenta);
            return pst.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException ex) {
            LOGGER.log(Level.SEVERE, "eliminarBancoCuenta: violación de constraint: ", ex.getMessage());
            return false;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error eliminarBancoCuenta: ", ex.getMessage());
            return false;
        }
    }

    public List<BancoCuenta> buscarCuentasBancarias(
            Long idBanco,
            String nroCuenta,
            String descripcion,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            Boolean estadoActivo) {
        
        System.out.println("idBanco"+ idBanco);
        System.out.println("nroCuenta"+nroCuenta);
        System.out.println("descripcion"+descripcion);
        System.out.println("fechaDesde"+fechaDesde);
        System.out.println("fechaHasta"+fechaHasta);
        System.out.println("estadoActivo"+estadoActivo);

        List<BancoCuenta> resultados = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT bc.* FROM banco_cuenta bc WHERE 1=1");

        List<Object> params = new ArrayList<>();

        // Filtro por banco (recomendado: por ID)
        if (idBanco != null) {
            sql.append(" AND bc.id_banco = ?");
            params.add(idBanco);
        }

        // Filtro por número de cuenta (búsqueda parcial)
        if (nroCuenta != null && !nroCuenta.trim().isEmpty()) {
            sql.append(" AND bc.nro_de_cuenta LIKE ?");
            params.add("%" + nroCuenta.trim() + "%");
        }

        // Filtro por descripción (búsqueda parcial)
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            sql.append(" AND bc.descripcion LIKE ?");
            params.add("%" + descripcion.trim() + "%");
        }

        // Rango de fechas
        if (fechaDesde != null) {
            sql.append(" AND bc.fecha >= ?");
            params.add(fechaDesde.atStartOfDay());   // 00:00:00
        }
        if (fechaHasta != null) {
            sql.append(" AND bc.fecha < ?");          // < día siguiente → inclusive
            params.add(fechaHasta.plusDays(1).atStartOfDay());
        }

        // Filtro por estado (si se pasa null → muestra todos)
        if (estadoActivo != null) {
            sql.append(" AND bc.estado = ?");
            params.add(estadoActivo ? 1 : 0);
        }

        // Ordenamiento recomendado (puedes cambiarlo según necesidad)
        sql.append(" ORDER BY bc.fecha DESC, bc.id_cuenta DESC");

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            // Asignar parámetros
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultados.add(mapearResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar cuentas bancarias", e);
            // Aquí podrías lanzar excepción personalizada si lo prefieres
        }

        return resultados;
    }

    public boolean existeBancoCuenta(String nroCuenta) {
        String sql = "SELECT id_cuenta FROM banco_cuenta u WHERE nro_de_cuenta = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nroCuenta);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Boolean.TRUE;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error existeBanco: ", ex.getMessage());
            ex.printStackTrace();
        }
        return Boolean.FALSE;
    }

    private BancoCuenta mapearResultSet(ResultSet rs) throws SQLException {
        BancoCuenta bancoCuenta = new BancoCuenta();
        bancoCuenta.setId_cuenta(rs.getLong("id_cuenta"));
        bancoCuenta.setFecha(rs.getTimestamp("fecha"));
        bancoCuenta.setNro_cuenta(rs.getString("nro_de_cuenta"));
        bancoCuenta.setMoneda(rs.getString("moneda"));
        bancoCuenta.setDescripcion(rs.getString("descripcion"));
        bancoCuenta.setId_banco(rs.getInt("id_banco"));
        bancoCuenta.setEstado(rs.getInt("estado"));
        return bancoCuenta;
    }

}
