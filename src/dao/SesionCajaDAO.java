package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.SesionCaja;
import util.ConexionDB;

public class SesionCajaDAO {

    /**
     * CORREGIDO: Obtener la ÚNICA sesión activa del día (solo puede haber UNA)
     *
     * @param idUsuario
     * @param fecha
     * @return SesionCaja
     */
    public SesionCaja obtenerSesionActivaDelDia(Integer idUsuario, String fecha) {
        String sql = "SELECT * FROM sesiones_caja "
                + "WHERE id_usuario = ? AND DATE(hora_inicio) = ? AND estado = 'ABIERTA' "
                + "ORDER BY numero_sesion_dia DESC LIMIT 1";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setString(2, fecha);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearResultSet(rs);
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtenerSesionActivaDelDia: " + ex.getMessage());
        }

        return null;
    }

    public boolean existeSesionAbiertaV2(Integer idUsuario, String fecha) {
        String sql = "SELECT id_sesion FROM sesiones_caja "
                + "WHERE id_usuario = ? AND DATE(hora_inicio) = ? AND estado = 'ABIERTA' "
                + "LIMIT 1";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setString(2, fecha);

            ResultSet rs = stmt.executeQuery();
            boolean existe = rs.next();

            System.out.println("✅ ¿Existe sesión abierta? " + existe);
            if (existe) {
                System.out.println("✅ ID de sesión abierta: " + rs.getLong("id_sesion"));
            }

            return existe;
        } catch (SQLException ex) {
            System.err.println("Error existeSesionAbiertaV2: " + ex.getMessage());
        }

        return false;
    }

    /**
     * Obtener último cierre del día
     *
     * @param idUsuario
     * @param fecha
     * @return boolean
     */
    public SesionCaja obtenerUltimoCierreDelDia(Integer idUsuario, String fecha) {
        String sql = "SELECT * FROM sesiones_caja "
                + "WHERE id_usuario = ? AND DATE(hora_inicio) = ? AND estado = 'CERRADA' "
                + "ORDER BY numero_sesion_dia DESC LIMIT 1";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setString(2, fecha);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearResultSet(rs);
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtenerUltimoCierreDelDia: " + ex.getMessage());
        }

        return null;
    }

    /**
     * Obtener último cierre general (cualquier día)
     *
     * @param idUsuario
     * @return SesionCaja
     */
    public SesionCaja obtenerUltimoCierreGeneral(Integer idUsuario) {
        String sql = "SELECT * FROM sesiones_caja "
                + "WHERE id_usuario = ? AND estado = 'CERRADA' "
                + "ORDER BY hora_fin DESC LIMIT 1";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearResultSet(rs);
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtenerUltimoCierreGeneral: " + ex.getMessage());
        }

        return null;
    }

    /**
     * Obtener siguiente número de sesión del día
     *
     * @param idUsuario
     * @param fecha
     * @return SesionCaja
     */
    public int obtenerSiguienteNumeroSesionDia(Integer idUsuario, String fecha) {
        String sql = "SELECT COALESCE(MAX(numero_sesion_dia), 0) + 1 FROM sesiones_caja "
                + "WHERE id_usuario = ? AND DATE(hora_inicio) = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idUsuario);
            stmt.setString(2, fecha);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtenerSiguienteNumeroSesionDia: " + ex.getMessage());
        }

        return 1;
    }

    /**
     * Insertar nueva sesión
     *
     * @param sesion
     * @return Integer
     */
    public Integer insertarSesion(SesionCaja sesion) {
        String sql = "INSERT INTO sesiones_caja (id_usuario, hora_inicio, saldo_inicial, estado, numero_sesion_dia, diferencia_turno) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, sesion.getIdUsuario());
            stmt.setTimestamp(2, sesion.getHoraInicio());
            stmt.setBigDecimal(3, sesion.getSaldoInicial());
            stmt.setString(4, sesion.getEstado());
            stmt.setInt(5, sesion.getNumeroSesionDia());
            stmt.setBigDecimal(6, sesion.getDiferenciaTurno());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al insertarSesion: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Actualizar sesión (para cierre)
     *
     * @param sesion
     * @return boolean
     */
    public boolean actualizarSesion(SesionCaja sesion) {
        String sql = "UPDATE sesiones_caja SET hora_fin = ?, saldo_final = ?, estado = ?, diferencia_turno = ? "
                + "WHERE id_sesion = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, sesion.getHoraFin());
            stmt.setBigDecimal(2, sesion.getSaldoFinal());
            stmt.setString(3, sesion.getEstado());
            stmt.setBigDecimal(4, sesion.getDiferenciaTurno());
            stmt.setLong(5, sesion.getIdSesion());

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error al actualizarSesion: " + ex.getMessage());
        }
        return Boolean.FALSE;
    }

    /**
     * Obtener todas las sesiones de un día (historial)
     *
     * @param idUsuario
     * @param fecha
     * @return boolean
     */
    public List<SesionCaja> obtenerSesionesPorDia(Integer idUsuario, String fecha) {
        String sql = "SELECT * FROM sesiones_caja "
                + "WHERE id_usuario = ? AND DATE(hora_inicio) = ? "
                + "ORDER BY numero_sesion_dia";

        List<SesionCaja> sesiones = new ArrayList<>();

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idUsuario);
            stmt.setString(2, fecha);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sesiones.add(mapearResultSet(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtenerSesionesPorDia: " + ex.getMessage());
        }

        return sesiones;
    }

    /**
     * Obtener una sesión específica por ID
     *
     * @param idSesion
     * @return SesionCaja
     */
    public SesionCaja obtenerSesionPorId(Integer idSesion) {
        String sql = "SELECT * FROM sesiones_caja WHERE id_sesion = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSesion);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearResultSet(rs);
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtenerSesionPorId: " + ex.getMessage());
        }

        return null;
    }
    
     /**
     * Obtener una sesión específica por ID de usuario
     *
     * @param idUsuario
     * @return SesionCaja
     */
    public SesionCaja obtenerSesionPorIdUsuario(Integer idUsuario) {
        String sql = "SELECT * FROM sesiones_caja WHERE id_usuario = ? order by numero_sesion_dia desc limit 1";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearResultSet(rs);
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtenerSesionPorId: " + ex.getMessage());
        }

        return null;
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

    /**
     * Mapear ResultSet a objeto SesionCaja
     */
    private SesionCaja mapearResultSet(ResultSet rs) throws SQLException {
        SesionCaja sesion = new SesionCaja();
        sesion.setIdSesion(rs.getInt("id_sesion"));
        sesion.setIdUsuario(rs.getInt("id_usuario"));
        sesion.setHoraInicio(rs.getTimestamp("hora_inicio"));
        sesion.setHoraFin(rs.getTimestamp("hora_fin"));
        sesion.setSaldoInicial(rs.getBigDecimal("saldo_inicial"));
        sesion.setSaldoFinal(rs.getBigDecimal("saldo_final"));
        sesion.setEstado(rs.getString("estado"));
        sesion.setNumeroSesionDia(rs.getInt("numero_sesion_dia"));
        sesion.setDiferenciaTurno(rs.getBigDecimal("diferencia_turno"));
        return sesion;
    }

}
