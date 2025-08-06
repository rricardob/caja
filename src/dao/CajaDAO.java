package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import util.ConexionDB;

public class CajaDAO {

    /**
     * Verifica si la caja esta abierta
     *
     * @return Boolean indicando el estado de la caja
     */
    public Boolean verificarEstadoCaja() {

        String sql = "SELECT COUNT(*) status FROM sesiones_caja\n"
                + "WHERE DATE(hora_inicio) = CURDATE() AND estado = 'ABIERTA'";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                if(rs.getInt("status") > 0){
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener permisos: " + e.getMessage());
        }

        return false;
    }
}
