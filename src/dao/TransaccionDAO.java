package dao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Transaccion;
import util.ConexionDB;

public class TransaccionDAO {

    private static final Logger LOGGER = Logger.getLogger(TransaccionDAO.class.getName());

    /**
     * Guarda una transacción con categoria de tipo INGRESO.
     */
    public int guardarIngreso(int idSesion, int idUsuario, int idCliente, BigDecimal importe, String descripcion, long idTipo) {
        Connection conn = null;
        PreparedStatement pstInsertTrans = null;
        PreparedStatement pstInsertRegistro = null;
        ResultSet rsKeys = null;
        try {
            conn = ConexionDB.obtenerConexion();
            conn.setAutoCommit(false);
            // Insertar en transacciones
            String sqlInsertTrans = "INSERT INTO transacciones (id_sesion, id_usuario, id_tipo, id_cliente, importe, descripcion) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            pstInsertTrans = conn.prepareStatement(sqlInsertTrans, Statement.RETURN_GENERATED_KEYS);
            pstInsertTrans.setInt(1, idSesion);
            pstInsertTrans.setInt(2, idUsuario);
            pstInsertTrans.setLong(3, idTipo);
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

            // Insertar registro de auditoría
            String sqlInsertRegistro = "INSERT INTO registros_transaccion (id_transaccion, accion, id_usuario) VALUES (?, ?, ?)";
            pstInsertRegistro = conn.prepareStatement(sqlInsertRegistro);
            pstInsertRegistro.setInt(1, idTransaccion);
            pstInsertRegistro.setString(2, "REGISTRADA");
            pstInsertRegistro.setInt(3, idUsuario);
            pstInsertRegistro.executeUpdate();

            conn.commit();
            return idTransaccion;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error guardarIngreso", e);
            try {
                if (conn != null) {
                    conn.rollback();
                    LOGGER.log(Level.FINE, "Rollback exitoso");
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.WARNING, "Rollback falló", ex);
            }
            return -1;
        } finally {
            try {
                if (rsKeys != null) {
                    rsKeys.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.FINE, "rsKeys.close fallo", e);
            }
            try {
                if (pstInsertTrans != null) {
                    pstInsertTrans.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.FINE, "pstInsertTrans.close fallo", e);
            }
            try {
                if (pstInsertRegistro != null) {
                    pstInsertRegistro.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.FINE, "pstInsertRegistro.close fallo", e);
            }
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.FINE, "conn.close fallo", e);
            }
        }
    }

    /**
     * Actualiza una transacción existente (importe, descripción, tipo)
     */
    public boolean actualizarTransaccion(Transaccion t) {
        if (t == null || t.getId_transaccion() <= 0) {
            LOGGER.log(Level.WARNING, "actualizarTransaccion: transacción inválida");
            return false;
        }

        String sql = "UPDATE transacciones SET id_tipo = ?, importe = ?, descripcion = ? WHERE id_transaccion = ?";
        Connection conn = null;
        PreparedStatement pstUpdate = null;
        PreparedStatement pstRegistro = null;

        try {
            conn = ConexionDB.obtenerConexion();
            conn.setAutoCommit(false);

            pstUpdate = conn.prepareStatement(sql);
            pstUpdate.setInt(1, t.getId_tipo());
            pstUpdate.setBigDecimal(2, t.getImporte());
            pstUpdate.setString(3, t.getDescripcion());
            pstUpdate.setInt(4, t.getId_transaccion());

            int affected = pstUpdate.executeUpdate();
            if (affected == 0) {
                conn.rollback();
                return false;
            }

            String sqlRegistro = "INSERT INTO registros_transaccion (id_transaccion, accion, id_usuario) VALUES (?, ?, ?)";
            pstRegistro = conn.prepareStatement(sqlRegistro);
            pstRegistro.setInt(1, t.getId_transaccion());
            pstRegistro.setString(2, "MODIFICADA");
            pstRegistro.setInt(3, t.getId_usuario());
            pstRegistro.executeUpdate();

            conn.commit();
            LOGGER.log(Level.INFO, "Transacción actualizada: {0}", t.getId_transaccion());
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error actualizarTransaccion", e);
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.WARNING, "Rollback falló en actualizarTransaccion", ex);
            }
            return false;
        } finally {
            try {
                if (pstUpdate != null) {
                    pstUpdate.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.FINE, "pstUpdate.close fallo", e);
            }
            try {
                if (pstRegistro != null) {
                    pstRegistro.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.FINE, "pstRegistro.close fallo", e);
            }
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.FINE, "conn.close fallo", e);
            }
        }
    }

    /**
     * Elimina una transacción por su ID.
     */
    public boolean eliminarTransaccion(int idTransaccion) {
        if (idTransaccion <= 0) {
            LOGGER.log(Level.WARNING, "eliminarTransaccion: ID inválido");
            return false;
        }

        Connection conn = null;
        PreparedStatement pstDelRegs = null;
        PreparedStatement pstDelTrans = null;

        try {
            conn = ConexionDB.obtenerConexion();
            conn.setAutoCommit(false);

            // 1) borrar hijos
            String sqlDelRegs = "DELETE FROM registros_transaccion WHERE id_transaccion = ?";
            pstDelRegs = conn.prepareStatement(sqlDelRegs);
            pstDelRegs.setInt(1, idTransaccion);
            pstDelRegs.executeUpdate();

            // 2) borrar padre
            String sqlDelTrans = "DELETE FROM transacciones WHERE id_transaccion = ?";
            pstDelTrans = conn.prepareStatement(sqlDelTrans);
            pstDelTrans.setInt(1, idTransaccion);
            boolean ok = pstDelTrans.executeUpdate() > 0;

            conn.commit();
            if (ok) {
                LOGGER.log(Level.INFO, "Transacción eliminada: {0}", idTransaccion);
            }
            return ok;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error eliminarTransaccion", ex);
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Rollback falló", e);
            }
            return false;
        } finally {
            try {
                if (pstDelRegs != null) {
                    pstDelRegs.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.FINE, "pstDelRegs.close fallo", e);
            }
            try {
                if (pstDelTrans != null) {
                    pstDelTrans.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.FINE, "pstDelTrans.close fallo", e);
            }
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.FINE, "conn.close fallo", e);
            }
        }
    }

    /**
     * Devuelve id_tipo para direccion = 'INGRESO' o -1 si no existe.
     */
    private long obtenerIdTipoPorCategoria(Connection conn, String categoriaNombre) throws SQLException {
        String sql = "SELECT tt.id_tipo "
                + "FROM tipo_transacciones tt "
                + "INNER JOIN categoria_transacciones ct ON tt.id_categoria_transacciones = ct.id_categoria_transacciones "
                + "WHERE ct.descripcion = ? AND tt.estado = 1 "
                + "LIMIT 1";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, categoriaNombre);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id_tipo");
                }
            }
        }
        return -1;
    }

    public List<Transaccion> listarIngresosPorSesion(int idSesion) {
        List<Transaccion> lista = new ArrayList<>();
        String sql
                = "SELECT t.id_transaccion, t.id_sesion, t.id_usuario, t.id_tipo, t.id_cliente, "
                + "       t.importe, t.descripcion, t.fecha_creacion, "
                + "       c.nombre_completo, c.doc_identidad, c.direccion, c.ruc, "
                + "       tt.descripcion AS tipo_descripcion "
                + "FROM transacciones t "
                + "JOIN clientes c ON t.id_cliente = c.id_cliente "
                + "JOIN tipo_transacciones tt ON t.id_tipo = tt.id_tipo "
                + "JOIN categoria_transacciones ct ON tt.id_categoria_transacciones = ct.id_categoria_transacciones "
                + "WHERE t.id_sesion = ? AND ct.descripcion IN ('INGRESO','REPOSICION') "
                + "ORDER BY t.fecha_creacion DESC";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idSesion);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error listarIngresosPorSesion", ex);
        }
        return lista;
    }

    public List<Transaccion> listarIngresosPorSesionExcludingClient(int idSesion, int excludeClientId) {
        List<Transaccion> lista = new ArrayList<>();
        String sql
                = "SELECT t.id_transaccion, t.id_sesion, t.id_usuario, t.id_tipo, t.id_cliente, "
                + "       t.importe, t.descripcion, t.fecha_creacion, "
                + "       c.nombre_completo, c.doc_identidad, c.direccion, c.ruc, "
                + "       tt.descripcion AS tipo_descripcion "
                + "FROM transacciones t "
                + "JOIN clientes c ON t.id_cliente = c.id_cliente "
                + "JOIN tipo_transacciones tt ON t.id_tipo = tt.id_tipo "
                + "JOIN categoria_transacciones ct ON tt.id_categoria_transacciones = ct.id_categoria_transacciones "
                + "WHERE t.id_sesion = ? AND ct.descripcion IN ('INGRESO','REPOSICION') AND t.id_cliente <> ? "
                + "ORDER BY t.fecha_creacion DESC";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idSesion);
            pst.setInt(2, excludeClientId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error listarIngresosPorSesionExcludingClient", ex);
        }
        return lista;
    }

    public Transaccion obtenerTransaccionPorId(int idTransaccion) {
        String sql
                = "SELECT t.id_transaccion, t.id_sesion, t.id_usuario, t.id_tipo, t.id_cliente, t.importe, t.descripcion, t.fecha_creacion, "
                + "       c.nombre_completo, c.doc_identidad, c.direccion, c.ruc, "
                + "       tt.descripcion AS tipo_descripcion "
                + "FROM transacciones t "
                + "JOIN clientes c ON t.id_cliente = c.id_cliente "
                + "JOIN tipo_transacciones tt ON t.id_tipo = tt.id_tipo "
                + "WHERE t.id_transaccion = ? LIMIT 1";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idTransaccion);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error obtenerTransaccionPorId", ex);
        }
        return null;
    }

    // Mapeo común de fila a objeto Transaccion
    private Transaccion mapRow(ResultSet rs) throws SQLException {
        Transaccion t = new Transaccion();
        t.setId_transaccion(rs.getInt("id_transaccion"));
        t.setId_sesion(rs.getInt("id_sesion"));
        t.setId_usuario(rs.getInt("id_usuario"));
        t.setId_tipo(rs.getInt("id_tipo"));
        t.setId_cliente(rs.getInt("id_cliente"));
        t.setImporte(rs.getBigDecimal("importe"));
        t.setDescripcion(rs.getString("descripcion"));
        t.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
        // Campos cliente transitorios
        t.setNombre_completo(rs.getString("nombre_completo"));
        t.setDoc_identidad(rs.getString("doc_identidad"));
        t.setDireccion(rs.getString("direccion"));
        t.setRuc(rs.getString("ruc"));
        // Campo Descripción del tipo transitorios 
        try {
            t.setTipoDescripcion(rs.getString("tipo_descripcion"));
        } catch (SQLException ignore) {
        }
        return t;
    }

    /**
     * Obtiene el calculo total de todas las transacciones segun la sesion
     */
    public BigDecimal calcularSaldoFinalDeSesion(Integer idSesion, BigDecimal saldoInicial) {
        String sql = "SELECT "
                + "  SUM(CASE WHEN ct.descripcion = 'INGRESO' THEN t.importe ELSE 0 END) as total_ingresos, "
                + "  SUM(CASE WHEN ct.descripcion = 'EGRESO' THEN t.importe ELSE 0 END) as total_egresos, "
                + "  COUNT(*) as total_transacciones "
                + "FROM transacciones t "
                + "INNER JOIN tipo_transacciones tt ON t.id_tipo = tt.id_tipo "
                + "INNER JOIN categoria_transacciones ct ON tt.id_categoria_transacciones = ct.id_categoria_transacciones "
                + "WHERE t.id_sesion = ? AND t.procesada_en_sesion = 0";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idSesion);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal totalIngresos = rs.getBigDecimal("total_ingresos");
                    BigDecimal totalEgresos = rs.getBigDecimal("total_egresos");

                    if (totalIngresos == null) {
                        totalIngresos = BigDecimal.ZERO;
                    }
                    if (totalEgresos == null) {
                        totalEgresos = BigDecimal.ZERO;
                    }

                    BigDecimal saldoFinal = saldoInicial.add(totalIngresos).subtract(totalEgresos);
                    return saldoFinal;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error calcularSaldoFinalDeSesion", ex);
        }
        return saldoInicial;
    }

    /**
     * Actualiza los registros que ya fueron calculados para el saldo total al
     * cierre de caja
     */
    public boolean actualizarTransaccionesProcesadas(Integer idSesion) {
        String sql = "update transacciones t set t.procesada_en_sesion = 1 where t.id_sesion = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSesion);

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizarTransaccionesProcesadas", ex);
        }
        return Boolean.FALSE;
    }

    /**
     * Obtiene el calculo total de todas las transacciones pendientes segun la
     * sesion
     */
    public boolean existeRegistrosPorProcesar(Integer idSesion) {
        String sql = "select count(*) as conteo from transacciones t where t.id_sesion = ? and t.procesada_en_sesion = 0";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSesion);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int conteo = rs.getInt("conteo");
                    return conteo > 0;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error existeRegistrosPorProcesar", ex);
        }

        return false;
    }

}
