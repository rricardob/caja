
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
import modelo.BancoDeposito;
import util.ConexionDB;

public class BancoDepositoDAO {
    
    private static final Logger LOGGER = Logger.getLogger(BancoDepositoDAO.class.getName());

    public Integer registrarBancoDeposito(BancoDeposito bancoDeposito) {

        String sql = "INSERT INTO banco_deposito (nro_voucher, id_cuenta, importe, id_usuario, terminal, estado) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, bancoDeposito.getNro_voucher());
            stmt.setLong(2, bancoDeposito.getId_cuenta());
            stmt.setDouble(3, bancoDeposito.getImporte());
            stmt.setLong(4, bancoDeposito.getId_usuario());
            stmt.setString(5, bancoDeposito.getTerminal());
            stmt.setInt(6, bancoDeposito.getEstado());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al insertar registrarBancoDeposito: ", ex.getMessage());
        }
        return null;
    }

    public boolean actualizarBancoDeposito(BancoDeposito bancoDeposito) {
        String sql = "UPDATE banco_deposito SET nro_voucher=?, id_cuenta=?, importe=?, id_usuario=?, terminal=?, estado=? WHERE id_deposito=?";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bancoDeposito.getNro_voucher());
            stmt.setLong(2, bancoDeposito.getId_cuenta());
            stmt.setDouble(3, bancoDeposito.getImporte());
            stmt.setLong(4, bancoDeposito.getId_usuario());
            stmt.setString(5, bancoDeposito.getTerminal());
            stmt.setInt(6, bancoDeposito.getEstado());
            stmt.setLong(7, bancoDeposito.getId_deposito());

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizar actualizarBancoDeposito: ", ex.getMessage());
        }
        return Boolean.FALSE;
    }

    public BancoDeposito obteneBancoDepositoPorId(Long idBancoDeposito) {
        String sql = "SELECT * FROM banco_deposito WHERE id_deposito = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idBancoDeposito);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearResultSet(rs);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obteneBancoDepositoPorId: ", ex.getMessage());
        }

        return null;
    }

    public boolean eliminarBancoDeposito(long idBancoDeposito) {
        if (idBancoDeposito <= 0) {
            return false;
        }
        String sql = "DELETE FROM banco_deposito WHERE id_deposito = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setLong(1, idBancoDeposito);
            return pst.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException ex) {
            LOGGER.log(Level.SEVERE, "eliminarBancoDeposito: violación de constraint: ", ex.getMessage());
            return false;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error eliminarBancoDeposito: ", ex.getMessage());
            return false;
        }
    }

    public List<BancoDeposito> buscarBancoDeposito(
            Long idCuenta,
            String nroVoucher,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            Boolean estadoActivo) {

        List<BancoDeposito> resultados = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT bd.* FROM banco_deposito bd WHERE 1=1");

        List<Object> params = new ArrayList<>();

        // Filtro por cuenta (recomendado: por ID)
        if (idCuenta != null) {
            sql.append(" AND bd.id_cuenta = ?");
            params.add(idCuenta);
        }

        // Filtro por número de cuenta (búsqueda parcial)
        if (nroVoucher != null && !nroVoucher.trim().isEmpty()) {
            sql.append(" AND bd.nroVoucher LIKE ?");
            params.add("%" + nroVoucher.trim() + "%");
        }

        // Rango de fechas
        if (fechaDesde != null) {
            sql.append(" AND bd.fecha >= ?");
            params.add(fechaDesde.atStartOfDay());   // 00:00:00
        }
        if (fechaHasta != null) {
            sql.append(" AND bd.fecha < ?");          // < día siguiente → inclusive
            params.add(fechaHasta.plusDays(1).atStartOfDay());
        }

        // Filtro por estado (si se pasa null → muestra todos)
        if (estadoActivo != null) {
            sql.append(" AND bd.estado = ?");
            params.add(estadoActivo ? 1 : 0);
        }

        // Ordenamiento recomendado (puedes cambiarlo según necesidad)
        sql.append(" ORDER BY bd.fecha DESC, bd.id_cuenta DESC");

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
            LOGGER.log(Level.SEVERE, "Error al buscar buscarBancoDeposito", e);
            // Aquí podrías lanzar excepción personalizada si lo prefieres
        }

        return resultados;
    }

    public boolean existeBancoDeposito(String nroVoucher) {
        String sql = "SELECT id_deposito FROM banco_deposito WHERE nro_voucher = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nroVoucher);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Boolean.TRUE;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error existeBancoDeposito: ", ex.getMessage());
            ex.printStackTrace();
        }
        return Boolean.FALSE;
    }

    private BancoDeposito mapearResultSet(ResultSet rs) throws SQLException {
        BancoDeposito bancoDeposito = new BancoDeposito();
        bancoDeposito.setId_deposito(rs.getLong("id_deposito"));
        bancoDeposito.setFecha(rs.getTimestamp("fecha"));
        bancoDeposito.setNro_voucher(rs.getString("nro_voucher"));
        bancoDeposito.setId_cuenta(rs.getLong("id_cuenta"));
        bancoDeposito.setImporte(rs.getDouble("importe"));
        bancoDeposito.setId_usuario(rs.getLong("id_usuario"));
        bancoDeposito.setTerminal(rs.getString("terminal"));
        bancoDeposito.setEstado(rs.getInt("estado"));
        return bancoDeposito;
    }
    
}
