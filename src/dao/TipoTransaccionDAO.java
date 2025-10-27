package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.TipoTransaccion;
import util.ConexionDB;

public class TipoTransaccionDAO {

    private static final Logger LOGGER = Logger.getLogger(TipoTransaccionDAO.class.getName());

    // SQL constants
    private static final String SQL_EXISTS_BY_DESCRIPCION
            = "SELECT 1 FROM tipo_transacciones WHERE descripcion = ? LIMIT 1";
    private static final String SQL_EXISTS_CATEGORIA_BY_ID
            = "SELECT 1 FROM categoria_transacciones WHERE id_categoria_transacciones = ? LIMIT 1";

    private boolean existsQuery(String sql, String param) {
        if (param == null || param.trim().isEmpty()) {
            return false;
        }
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, param);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "existsQuery fallo (sql={0}, param={1}) : {2}",
                    new Object[]{sql, param, e.toString()});
            LOGGER.log(Level.FINE, "Detalle", e);
            return false;
        }
    }

    public boolean existsByDescripcion(String descripcion) {
        return existsQuery(SQL_EXISTS_BY_DESCRIPCION, descripcion);
    }

    public boolean existsByDescripcionExceptId(String descripcion, long idExcept) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return false;
        }
        String sql = "SELECT 1 FROM tipo_transacciones WHERE descripcion = ? AND id_tipo <> ? LIMIT 1";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, descripcion);
            pst.setLong(2, idExcept);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "existsByDescripcionExceptId fallo: {0}", e.toString());
            LOGGER.log(Level.FINE, "Detalle", e);
            return false;
        }
    }

    public boolean existsCategoriaById(long idCategoria) {
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(SQL_EXISTS_CATEGORIA_BY_ID)) {
            pst.setLong(1, idCategoria);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "existsCategoriaById fallo: {0}", e.toString());
            LOGGER.log(Level.FINE, "Detalle", e);
            return false;
        }
    }

    public TipoTransaccion insertar(TipoTransaccion t) {
        if (t == null) {
            LOGGER.log(Level.WARNING, "insertar: tipo_transaccion nulo.");
            return null;
        }
        String sql = "INSERT INTO tipo_transacciones (descripcion, estado, id_categoria_transacciones) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, t.getDescripcion());
            pst.setBoolean(2, Boolean.TRUE.equals(t.getEstado()));
            pst.setLong(3, t.getId_categoria_transacciones());

            int affected = pst.executeUpdate();
            if (affected == 0) {
                LOGGER.log(Level.WARNING, "insertar: no se insertó ningún registro.");
                return null;
            }
            try (ResultSet rsKeys = pst.getGeneratedKeys()) {
                if (rsKeys.next()) {
                    long id = rsKeys.getLong(1);
                    t.setId_tipo(id);
                    LOGGER.log(Level.INFO, "TipoTransaccion insertado con id: {0}", id);
                    return t;
                } else {
                    LOGGER.log(Level.WARNING, "insertar: no se obtuvo key generada.");
                    return null;
                }
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            LOGGER.log(Level.WARNING, "insertar: violación de constraint (posible duplicado/ FK): {0}", ex.getMessage());
            LOGGER.log(Level.FINE, "Detalle", ex);
            return null;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error insertar tipo_transacciones: {0}", ex.toString());
            LOGGER.log(Level.FINE, "Detalle", ex);
            return null;
        }
    }

    public boolean actualizar(TipoTransaccion t) {
        if (t == null || t.getId_tipo() <= 0) {
            return false;
        }
        String sql = "UPDATE tipo_transacciones SET descripcion = ?, estado = ?, id_categoria_transacciones = ? WHERE id_tipo = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, t.getDescripcion());
            pst.setBoolean(2, Boolean.TRUE.equals(t.getEstado()));
            pst.setLong(3, t.getId_categoria_transacciones());
            pst.setLong(4, t.getId_tipo());
            return pst.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException ex) {
            LOGGER.log(Level.WARNING, "actualizar: violación de constraint: {0}", ex.getMessage());
            LOGGER.log(Level.FINE, "Detalle", ex);
            return false;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error actualizar tipo_transacciones", ex);
            return false;
        }
    }

    public boolean eliminarPorId(long id) {
        if (id <= 0) {
            return false;
        }
        String sql = "DELETE FROM tipo_transacciones WHERE id_tipo = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setLong(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error eliminar tipo_transacciones", ex);
            return false;
        }
    }

    public TipoTransaccion buscarPorId(long id) {
        String sql = "SELECT id_tipo, descripcion, estado, fecha_creacion, fecha_actualizacion, id_categoria_transacciones "
                + "FROM tipo_transacciones WHERE id_tipo = ? LIMIT 1";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error buscarPorId tipo_transacciones", ex);
        }
        return null;
    }

    public TipoTransaccion buscarPorDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return null;
        }
        String sql = "SELECT id_tipo, descripcion, estado, fecha_creacion, fecha_actualizacion, id_categoria_transacciones "
                + "FROM tipo_transacciones WHERE descripcion = ? LIMIT 1";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, descripcion);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error buscarPorDescripcion tipo_transacciones", ex);
        }
        return null;
    }

    public List<TipoTransaccion> listarTodos() {
        List<TipoTransaccion> lista = new ArrayList<>();
        String sql = "SELECT id_tipo, descripcion, estado, fecha_creacion, fecha_actualizacion, id_categoria_transacciones "
                + "FROM tipo_transacciones ORDER BY descripcion ASC";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error listarTodos tipo_transacciones", ex);
        }
        return lista;
    }

    /**
     * Lista tipos de transacción activos filtrados por múltiples categorías.
     * lista de nombres de categorías ("INGRESO", "EGRESO", "REPOSICION")
     */
    public List<TipoTransaccion> listarPorCategorias(List<String> categoriasDescripcion) {
        if (categoriasDescripcion == null || categoriasDescripcion.isEmpty()) {
            return new ArrayList<>();
        }

        List<TipoTransaccion> lista = new ArrayList<>();
        String placeholders = String.join(", ", java.util.Collections.nCopies(categoriasDescripcion.size(), "?"));
        String sql = "SELECT tt.id_tipo, tt.descripcion, tt.estado, tt.fecha_creacion, tt.fecha_actualizacion, tt.id_categoria_transacciones "
                + "FROM tipo_transacciones tt "
                + "INNER JOIN categoria_transacciones ct ON tt.id_categoria_transacciones = ct.id_categoria_transacciones "
                + "WHERE ct.descripcion IN (" + placeholders + ") AND tt.estado = 1 "
                + "ORDER BY tt.descripcion ASC";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            for (int i = 0; i < categoriasDescripcion.size(); i++) {
                pst.setString(i + 1, categoriasDescripcion.get(i));
            }
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error listarPorCategorias", ex);
        }
        return lista;
    }

    private TipoTransaccion mapRow(ResultSet rs) throws SQLException {
        TipoTransaccion t = new TipoTransaccion();
        t.setId_tipo(rs.getLong("id_tipo"));
        t.setDescripcion(rs.getString("descripcion"));
        t.setEstado(rs.getBoolean("estado"));
        t.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
        t.setFecha_actualizacion(rs.getTimestamp("fecha_actualizacion"));
        t.setId_categoria_transacciones(rs.getLong("id_categoria_transacciones"));
        return t;
    }

}
