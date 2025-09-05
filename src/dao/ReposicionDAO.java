package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Reposicion;
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

}
