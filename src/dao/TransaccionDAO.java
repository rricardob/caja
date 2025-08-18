package dao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * Lista las transacciones de tipo INGRESO para la sesión dada. Retorna
     * lista vacía si no hay resultados o en caso de error.
     *
     * @param idSesion
     */
    public List<Transaccion> listarIngresosPorSesion(int idSesion) {
        List<Transaccion> lista = new ArrayList<>();
        String sql = "SELECT t.id_transaccion, t.id_sesion, t.id_usuario, t.id_tipo, t.id_cliente, t.importe, t.descripcion, t.fecha_creacion, "
                + "c.nombre_completo, c.doc_identidad, c.direccion, c.ruc "
                + "FROM transacciones t "
                + "JOIN clientes c ON t.id_cliente = c.id_cliente "
                + "WHERE t.id_sesion = ? AND t.id_tipo = ? "
                + "ORDER BY t.fecha_creacion DESC";
        try (Connection conn = ConexionDB.obtenerConexion()) {
            int idTipo = obtenerIdTipoIngreso(conn);
            if (idTipo == -1) {
                return lista;
            }
            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setInt(1, idSesion);
                pst.setInt(2, idTipo);
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        Transaccion t = new Transaccion();
                        t.setId_transaccion(rs.getInt("id_transaccion"));
                        t.setId_sesion(rs.getInt("id_sesion"));
                        t.setId_usuario(rs.getInt("id_usuario"));
                        t.setId_tipo(rs.getInt("id_tipo"));
                        t.setId_cliente(rs.getInt("id_cliente"));
                        t.setImporte(rs.getBigDecimal("importe"));
                        t.setDescripcion(rs.getString("descripcion"));
                        t.setFecha_creacion(rs.getTimestamp("fecha_creacion"));

                        // Campos cliente (transitorios)
                        t.setNombre_completo(rs.getString("nombre_completo"));
                        t.setDoc_identidad(rs.getString("doc_identidad"));
                        t.setRuc(rs.getString("ruc"));
                        t.setDireccion(rs.getString("direccion"));

                        lista.add(t);
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error listarIngresosPorSesion: " + ex.getMessage());
            // devuelve lista (posiblemente vacía)
        }
        return lista;
    }

    /**
     * Obtiene una transaccion (tipo ingreso) por su id, incluyendo datos del
     * cliente. Retorna null si no existe o en error.
     */
    public Transaccion obtenerTransaccionPorId(int idTransaccion) {
        String sql = "SELECT t.id_transaccion, t.id_sesion, t.id_usuario, t.id_tipo, t.id_cliente, t.importe, t.descripcion, t.fecha_creacion, "
                + "c.nombre_completo, c.doc_identidad, c.direccion, c.ruc "
                + "FROM transacciones t "
                + "JOIN clientes c ON t.id_cliente = c.id_cliente "
                + "WHERE t.id_transaccion = ? LIMIT 1";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idTransaccion);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Transaccion t = new Transaccion();
                    t.setId_transaccion(rs.getInt("id_transaccion"));
                    t.setId_sesion(rs.getInt("id_sesion"));
                    t.setId_usuario(rs.getInt("id_usuario"));
                    t.setId_tipo(rs.getInt("id_tipo"));
                    t.setId_cliente(rs.getInt("id_cliente"));
                    t.setImporte(rs.getBigDecimal("importe"));
                    t.setDescripcion(rs.getString("descripcion"));
                    t.setFecha_creacion(rs.getTimestamp("fecha_creacion"));

                    // Campos cliente (transitorios)
                    t.setNombre_completo(rs.getString("nombre_completo"));
                    t.setDoc_identidad(rs.getString("doc_identidad"));
                    t.setRuc(rs.getString("ruc"));
                    t.setDireccion(rs.getString("direccion"));

                    return t;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error obtenerTransaccionPorId: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Obtiene el calculo total de todas las transacciones segun la sesion
     * @param idSesion
     * @param saldoInicial
     *
     * @return BigDecimal
     */
    public BigDecimal calcularSaldoFinalDeSesion(Integer idSesion, BigDecimal saldoInicial) {
        String sql = "SELECT " +
                    "    SUM(CASE WHEN tt.direccion = 'INGRESO' THEN t.importe ELSE 0 END) as total_ingresos, " +
                    "    SUM(CASE WHEN tt.direccion = 'EGRESO' THEN t.importe ELSE 0 END) as total_egresos, " +
                    "    COUNT(*) as total_transacciones " +
                    "FROM transacciones t " +
                    "INNER JOIN tipos_transaccion tt ON t.id_tipo = tt.id_tipo " +
                    "WHERE t.id_sesion = ? and t.procesada_en_sesion = 0";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idSesion);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                BigDecimal totalIngresos = rs.getBigDecimal("total_ingresos");
                BigDecimal totalEgresos = rs.getBigDecimal("total_egresos");
                int totalTransacciones = rs.getInt("total_transacciones");
                
                if (totalIngresos == null) totalIngresos = BigDecimal.ZERO;
                if (totalEgresos == null) totalEgresos = BigDecimal.ZERO;
                
                BigDecimal saldoFinal = saldoInicial.add(totalIngresos).subtract(totalEgresos); 
                return saldoFinal;
            }
        } catch (SQLException ex) {
            System.err.println("Error calcularSaldoFinalDeSesion: " + ex.getMessage());
        }
        
        return saldoInicial;
    }
    
    
    /**
     * Actualiza los registros que ya fueron calculados para el saldo total al cierre de caja
     * @param idSesion
     *
     * @return BigDecimal
     */
    public boolean actualizarTransaccionesProcesadas(Integer idSesion) {
        String sql = "update transacciones t set t.procesada_en_sesion = 1 where t.id_sesion = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSesion);

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error al actualizarSesion: " + ex.getMessage());
        }
        return Boolean.FALSE;
    }
    
    
    /**
     * Obtiene el calculo total de todas las transacciones pendientes segun la sesion
     * @param idSesion
     *
     * @return BigDecimal
     */
    public boolean existeRegistrosPorProcesar(Integer idSesion) {
        String sql = "select count(*) as conteo from transacciones t where t.id_sesion = ? and t.procesada_en_sesion = 0";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSesion);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int conteo = rs.getInt("conteo");
                return conteo > 0;
            }
            
        } catch (SQLException ex) {
            System.err.println("Error existeSesionAbiertaV2: " + ex.getMessage());
        }

        return false;
    }

}
