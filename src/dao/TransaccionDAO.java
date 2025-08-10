package dao;

import java.math.BigDecimal;
import java.sql.*;
import modelo.Transaccion;
import util.ConexionDB;

public class TransaccionDAO {

    /**
     * Guarda una transacción de tipo INGRESO.
     *
     * @param idSesion id de la sesión de caja
     * @param idUsuario id del usuario que registra
     * @param idCliente id del cliente
     * @param importe importe del ingreso
     * @param descripcion descripción
     * @return id_transaccion creada o -1 en error
     */
    public int guardarIngreso(int idSesion, int idUsuario, int idCliente, BigDecimal importe, String descripcion) {
        Connection conn = null;
        PreparedStatement pstInsertTrans = null;
        PreparedStatement pstInsertRegistro = null;
        ResultSet rsKeys = null;

        try {
            conn = ConexionDB.obtenerConexion();
            conn.setAutoCommit(false);

            // 1) obtener id_tipo para direccion = 'INGRESO'
            int idTipo = obtenerIdTipoIngreso(conn);
            if (idTipo == -1) {
                conn.rollback();
                return -1;
            }

            // 2) insertar en transacciones
            String sqlInsertTrans = "INSERT INTO transacciones (id_sesion, id_usuario, id_tipo, id_cliente, importe, descripcion) VALUES (?, ?, ?, ?, ?, ?)";
            pstInsertTrans = conn.prepareStatement(sqlInsertTrans, Statement.RETURN_GENERATED_KEYS);
            pstInsertTrans.setInt(1, idSesion);
            pstInsertTrans.setInt(2, idUsuario);
            pstInsertTrans.setInt(3, idTipo);
            pstInsertTrans.setInt(4, idCliente);
            pstInsertTrans.setBigDecimal(5, importe);
            pstInsertTrans.setString(6, descripcion);
            int affected = pstInsertTrans.executeUpdate();
            if (affected == 0) {
                conn.rollback();
                return -1;
            }
            rsKeys = pstInsertTrans.getGeneratedKeys();
            int idTransaccion = -1;
            if (rsKeys.next()) {
                idTransaccion = rsKeys.getInt(1);
            } else {
                conn.rollback();
                return -1;
            }

            // 3) insertar registro de auditoría
            String sqlInsertRegistro = "INSERT INTO registros_transaccion (id_transaccion, accion, id_usuario) VALUES (?, ?, ?)";
            pstInsertRegistro = conn.prepareStatement(sqlInsertRegistro);
            pstInsertRegistro.setInt(1, idTransaccion);
            pstInsertRegistro.setString(2, "REGISTRADA");
            pstInsertRegistro.setInt(3, idUsuario);
            pstInsertRegistro.executeUpdate();

            conn.commit();
            return idTransaccion;

        } catch (SQLException e) {
            System.err.println("Error guardarIngreso: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                /* ignore */ }
            return -1;
        } finally {
            try {
                if (rsKeys != null) {
                    rsKeys.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (pstInsertTrans != null) {
                    pstInsertTrans.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (pstInsertRegistro != null) {
                    pstInsertRegistro.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    /**
     * Devuelve id_tipo para direccion = 'INGRESO' o -1 si no existe.
     */
    private int obtenerIdTipoIngreso(Connection conn) throws SQLException {
        String sql = "SELECT id_tipo FROM tipos_transaccion WHERE direccion = 'INGRESO' LIMIT 1";
        try (PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id_tipo");
            }
        }
        return -1;
    }
}
