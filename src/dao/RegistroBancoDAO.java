package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Banco;
import util.ConexionDB;

public class RegistroBancoDAO {

    private static final Logger LOGGER = Logger.getLogger(RegistroBancoDAO.class.getName());

    public Integer registrarBanco(Banco banco) {

        String sql = "INSERT INTO banco (descripcion, estado) VALUES (?, ?)";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, banco.getDescripcion());
            stmt.setInt(2, banco.getEstado());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al insertar banco: ", ex.getMessage());
        }
        return null;
    }

    public boolean actualizarBanco(Banco banco) {
        String sql = "UPDATE banco SET descripcion=?, estado=? WHERE id_banco=?";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, banco.getDescripcion());
            stmt.setInt(2, banco.getEstado());
            stmt.setLong(3, banco.getId_banco());

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizarBanco: ", ex.getMessage());
        }
        return Boolean.FALSE;
    }

    public Banco obteneBancoPorId(Long idBanco) {
        String sql = "SELECT * FROM banco WHERE id_banco = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idBanco);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearResultSet(rs);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtenerSesionPorId: ", ex.getMessage());
        }

        return null;
    }

    public boolean eliminarBanco(long idBanco) {
        if (idBanco <= 0) {
            return false;
        }
        String sql = "DELETE FROM banco WHERE id_banco = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setLong(1, idBanco);
            return pst.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException ex) {
            LOGGER.log(Level.SEVERE, "eliminarBanco: violación de constraint: ", ex.getMessage());
            return false;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error eliminarBanco: ", ex.getMessage());
            return false;
        }
    }

    public List<Banco> obtenerBancos(String nombreBanco) {
        StringBuilder sqlBuilder = new StringBuilder("select * from banco");

        boolean agregarWhereBanco = (nombreBanco != null && !nombreBanco.trim().isEmpty());

        if (agregarWhereBanco) {
            sqlBuilder.append(" WHERE (descripcion LIKE ?)");  // Solo agregamos si hay parámetro
        }

        //sqlBuilder.append(" ORDER BY nombre_usuario DESC");  // Ordenamiento al final
        List<Banco> sesiones = new ArrayList<>();

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {

            int paramIndex = 1;

            if (agregarWhereBanco) {
                String param = "%" + nombreBanco + "%";  // Coincidencia en cualquier parte
                stmt.setString(paramIndex++, param);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sesiones.add(mapearResultSet(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener bancos: ", ex.getMessage());
        }
        return sesiones;
    }

    public boolean existeBanco(String nombreBanco) {
        String sql = "SELECT id_banco FROM banco u WHERE descripcion = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombreBanco);
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

    private Banco mapearResultSet(ResultSet rs) throws SQLException {
        Banco banco = new Banco();
        banco.setId_banco(rs.getLong("id_banco"));
        banco.setDescripcion(rs.getString("descripcion"));
        banco.setEstado(rs.getInt("estado"));
        return banco;
    }

}
