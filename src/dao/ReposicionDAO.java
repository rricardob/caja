package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Reposicion;
import modelo.Transaccion;
import util.ConexionDB;

public class ReposicionDAO {

    private static final Logger LOGGER = Logger.getLogger(ReposicionDAO.class.getName());

    public Reposicion insertarReposicion(int idTransaccion) {

        // SQL de inserción
        String sql = "INSERT INTO reposicionamientos (id_transaccion) VALUES(?)";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, idTransaccion);

            int affected = pst.executeUpdate();
            if (affected == 0) {
                LOGGER.log(Level.WARNING, "insertarReposicion: no se insertó ningún registro.");
                return null;
            }

            try (ResultSet rsKeys = pst.getGeneratedKeys()) {
                if (rsKeys.next()) {
                    int id = rsKeys.getInt(1);
                    LOGGER.log(Level.INFO, "Reposicion insertado con id: {0}", id);
                    Reposicion reposicion = new Reposicion();
                    reposicion.setId_reposicionamiento(id);
                    return reposicion;
                } else {
                    LOGGER.log(Level.WARNING, "insertarReposicion: no se obtuvo key generada.");
                    return null;
                }
            }

        } catch (SQLIntegrityConstraintViolationException ex) {
            // Violación de constraint única (duplicado) — maepamos los logs 
            LOGGER.log(Level.WARNING, "insertarCliente: violación de constraint (posible duplicado): {0}", ex.getMessage());
            LOGGER.log(Level.FINE, "Detalle", ex);
            return null;
        } catch (SQLException ex) {
            // Error general de BD
            LOGGER.log(Level.SEVERE, "Error insertarReposicion: {0}", ex.toString());
            LOGGER.log(Level.FINE, "Detalle", ex);
            return null;
        }
    }

    public List<Reposicion> recuperarReposiciones(Date fechaInicio, Date fechaFin) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder("select \n"
                + "r.id_reposicionamiento, t.id_transaccion, r.mostrado_en_pantalla, r.fecha_creacion, \n"
                + "u.nombre_completo , r.fecha_aprobacion, r.aprobado_por, t.importe, t.descripcion \n"
                + "from reposicionamientos r \n"
                + "join transacciones t on t.id_transaccion = r.id_transaccion \n"
                + "join usuarios u on u.id_usuario = t.id_usuario");

        boolean hasFilters = false;
        if (fechaInicio != null || fechaFin != null) {
            sqlBuilder.append(" WHERE ");
            hasFilters = true;
        }

        if (fechaInicio != null) {
            sqlBuilder.append("DATE(r.fecha_creacion) >= ?");
        }

        if (fechaFin != null) {
            if (fechaInicio != null) {
                sqlBuilder.append(" AND ");
            }
            sqlBuilder.append("DATE(r.fecha_creacion) <= ?");
        }

        List<Reposicion> reposiciones = new ArrayList<>();

        try (Connection conn = ConexionDB.obtenerConexion()) {
            try (PreparedStatement pst = conn.prepareStatement(sqlBuilder.toString())) {
                
                int paramIndex = 1;

                if (fechaInicio != null) {
                    pst.setDate(paramIndex++, new java.sql.Date(fechaInicio.getTime()));
                }

                if (fechaFin != null) {
                    pst.setDate(paramIndex, new java.sql.Date(fechaFin.getTime()));
                }
                
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        reposiciones.add(mapearResultSetAListaReposiciones(rs));
                    }
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error recuperarReposiciones", ex);
            return new ArrayList<>();
        }

        return reposiciones;

    }

    private Reposicion mapearResultSetAListaReposiciones(ResultSet rs) throws SQLException {
        Transaccion transaccion = new Transaccion();
        transaccion.setId_transaccion(rs.getInt("id_transaccion"));
        transaccion.setNombre_completo(rs.getString("nombre_completo"));
        transaccion.setImporte(rs.getBigDecimal("importe"));
        transaccion.setDescripcion(rs.getString("descripcion"));

        Reposicion reposicion = new Reposicion();
        reposicion.setId_reposicionamiento(rs.getInt("id_reposicionamiento"));
        reposicion.setTransaccion(transaccion);
        reposicion.setMostrado_en_pantalla(rs.getBoolean("mostrado_en_pantalla"));
        reposicion.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
        reposicion.setFecha_aprobacion(rs.getTimestamp("fecha_aprobacion"));
        reposicion.setAprobado_por(rs.getInt("aprobado_por"));
        return reposicion;
    }

    public boolean acta(Reposicion reposicion) {
        String sql = "update reposicionamientos r "
                + "set r.fecha_aprobacion = current_timestamp(), r.aprobado_por = ?, r.mostrado_en_pantalla = ?\n"
                + "where r.id_reposicionamiento  = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reposicion.getAprobado_por());
            stmt.setBoolean(2, reposicion.isMostrado_en_pantalla());
            stmt.setInt(3, reposicion.getId_reposicionamiento());

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error al actualizarSesion: " + ex.getMessage());
        }
        return Boolean.FALSE;
    }
}
