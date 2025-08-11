package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
                if (rs.getInt("status") > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener permisos: " + e.getMessage());
        }

        return false;
    }

    /**
     * Obtiene el id_sesion de la sesión ABIERTA para el usuario (si existe). Si
     * no hay sesión para el usuario devuelve -1.
     */
    public int obtenerIdSesionActivaPorUsuario(int idUsuario) {
        String sql = "SELECT id_sesion FROM sesiones_caja WHERE id_usuario = ? AND estado = 'ABIERTA' ORDER BY hora_inicio DESC LIMIT 1";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idUsuario);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_sesion");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerIdSesionActivaPorUsuario: " + e.getMessage());
        }
        return -1;
    }

    public int actualizarCaja(Integer usuario_id, Timestamp fecha_inicio, Timestamp fecha_fin, Double saldo_inicio, Double saldo_final, String estado) {
        String sql = "INSERT INTO sesiones_caja\n"
                + "(id_usuario, hora_inicio, hora_fin, saldo_inicial, saldo_final, estado)\n"
                + "VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, usuario_id);
            pst.setTimestamp(2, fecha_inicio);
            pst.setTimestamp(3, fecha_fin);
            pst.setDouble(4, saldo_inicio);
            pst.setDouble(5, saldo_final);
            pst.setString(6, estado);

            return pst.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error obtenerIdSesionActivaPorUsuario: " + e.getMessage());
        }
        return -1;
    }

}
