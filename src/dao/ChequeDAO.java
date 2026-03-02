package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Cheque;
import util.ConexionDB;

public class ChequeDAO {

    private static final Logger LOGGER = Logger.getLogger(ChequeDAO.class.getName());

    /**
     * Guarda un nuevo cheque en la base de datos.
     */
    public int guardar(Cheque cheque) {
        String sql = "INSERT INTO cheques (id_usuario, nro_cheque, fecha_emision, saldo_anterior, total_cheque) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, cheque.getId_usuario());
            ps.setString(2, cheque.getNro_cheque());
            ps.setDate(3, cheque.getFecha_emision());
            ps.setBigDecimal(4, cheque.getSaldo_anterior());
            ps.setBigDecimal(5, cheque.getTotal_cheque());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                return -1;
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return (int) generatedKeys.getLong(1);
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar cheque: " + e.getMessage(), e);
            return -1;
        }
    }

    /**
     * Lista todos los cheques registrados.
     */
    public List<Cheque> listar() {
        List<Cheque> lista = new ArrayList<>();
        String sql = "SELECT c.*, u.nombre_usuario FROM cheques c " +
                "JOIN usuario u ON c.id_usuario = u.id_usuario " +
                "ORDER BY c.fecha_creacion DESC";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al listar cheques: " + e.getMessage(), e);
        }
        return lista;
    }

    /**
     * Lista cheques con filtros opcionales por rango de fechas y número de cheque.
     */
    public List<Cheque> listarConFiltros(java.sql.Date inicio, java.sql.Date fin, String nroCheque) {
        List<Cheque> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT c.*, u.nombre_usuario FROM cheques c ")
                .append("JOIN usuario u ON c.id_usuario = u.id_usuario ")
                .append("WHERE 1=1 ");

        if (inicio != null) {
            sql.append("AND c.fecha_emision >= ? ");
        }
        if (fin != null) {
            sql.append("AND c.fecha_emision <= ? ");
        }
        if (nroCheque != null && !nroCheque.trim().isEmpty()) {
            sql.append("AND c.nro_cheque LIKE ? ");
        }

        sql.append("ORDER BY c.fecha_emision DESC, c.fecha_creacion DESC");

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (inicio != null) {
                ps.setDate(paramIndex++, inicio);
            }
            if (fin != null) {
                ps.setDate(paramIndex++, fin);
            }
            if (nroCheque != null && !nroCheque.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + nroCheque.trim() + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al filtrar cheques: " + e.getMessage(), e);
        }
        return lista;
    }

    private Cheque mapRow(ResultSet rs) throws SQLException {
        Cheque c = new Cheque();
        c.setId_cheque(rs.getLong("id_cheque"));
        c.setId_usuario(rs.getLong("id_usuario"));
        c.setNro_cheque(rs.getString("nro_cheque"));
        c.setFecha_emision(rs.getDate("fecha_emision"));
        c.setSaldo_anterior(rs.getBigDecimal("saldo_anterior"));
        c.setTotal_cheque(rs.getBigDecimal("total_cheque"));
        c.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
        c.setFecha_actualizacion(rs.getTimestamp("fecha_actualizacion"));

        // Atributo transitorio
        try {
            c.setNombre_usuario(rs.getString("nombre_usuario"));
        } catch (SQLException e) {
            // Ignorar si la columna no existe en el ResultSet (depende del JOIN)
        }

        return c;
    }

    /**
     * Actualiza un cheque existente.
     */
    public boolean actualizar(Cheque cheque) {
        String sql = "UPDATE cheques SET nro_cheque = ?, fecha_emision = ?, saldo_anterior = ?, total_cheque = ?, fecha_actualizacion = CURRENT_TIMESTAMP WHERE id_cheque = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cheque.getNro_cheque());
            ps.setDate(2, cheque.getFecha_emision());
            ps.setBigDecimal(3, cheque.getSaldo_anterior());
            ps.setBigDecimal(4, cheque.getTotal_cheque());
            ps.setLong(5, cheque.getId_cheque());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar cheque: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Elimina un cheque por su ID.
     */
    public boolean eliminar(long idCheque) {
        String sql = "DELETE FROM cheques WHERE id_cheque = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, idCheque);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar cheque: " + e.getMessage(), e);
            return false;
        }
    }
}
