package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Cliente;
import util.ConexionDB;

public class ClienteDAO {

    private static final Logger LOGGER = Logger.getLogger(ClienteDAO.class.getName());

    // SQL constants
    private static final String SQL_EXISTS_BY_NOMBRE = "SELECT 1 FROM clientes WHERE nombre_completo = ? LIMIT 1";
    private static final String SQL_EXISTS_BY_DIRECCION = "SELECT 1 FROM clientes WHERE direccion = ? LIMIT 1";
    private static final String SQL_EXISTS_BY_DOC = "SELECT 1 FROM clientes WHERE doc_identidad = ? LIMIT 1";
    private static final String SQL_EXISTS_BY_RUC = "SELECT 1 FROM clientes WHERE ruc = ? LIMIT 1";
    private static final String SQL_EXISTS_BY_TELEFONO = "SELECT 1 FROM clientes WHERE telefono = ? LIMIT 1";

    /**
     * Helpers genéricos (así evitamos código DRY)
     */
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
            LOGGER.log(Level.SEVERE, "existsQuery fallo (sql={0}, param={1}) : {2}", new Object[]{sql, param, e.toString()});
            LOGGER.log(Level.FINE, "Detalle", e);
            return false;
        }
    }

    public boolean existsByNombre(String nombre) {
        return existsQuery(SQL_EXISTS_BY_NOMBRE, nombre);
    }

    public boolean existsByDireccion(String direccion) {
        return existsQuery(SQL_EXISTS_BY_DIRECCION, direccion);
    }

    public boolean existsByDoc(String doc) {
        return existsQuery(SQL_EXISTS_BY_DOC, doc);
    }

    public boolean existsByRuc(String ruc) {
        return existsQuery(SQL_EXISTS_BY_RUC, ruc);
    }

    public boolean existsByTelefono(String tel) {
        return existsQuery(SQL_EXISTS_BY_TELEFONO, tel);
    }

    /**
     * Busca un cliente por su identificador (doc o ruc)
     */
    public Cliente buscarPorIdentificador(String identificador) {
        if (identificador == null || identificador.trim().isEmpty()) {
            return null;
        }
        String sql = "SELECT id_cliente, nombre_completo, direccion, doc_identidad, telefono, ruc "
                + "FROM clientes WHERE doc_identidad = ? OR ruc = ? LIMIT 1";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, identificador);
            pst.setString(2, identificador);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new Cliente();
                    c.setId_cliente(rs.getInt("id_cliente"));
                    c.setNombre_completo(rs.getString("nombre_completo"));
                    c.setDireccion(rs.getString("direccion"));
                    c.setDoc_identidad(rs.getString("doc_identidad"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setRuc(rs.getString("ruc"));
                    return c;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error buscarPorIdentificador: {0}", e.toString());
            LOGGER.log(Level.FINE, "Detalle", e);
        }
        return null;
    }

    /**
     * Inserta un nuevo cliente y devuelve el Cliente con id asignado (o null en
     * error).
     */
    public Cliente insertarCliente(Cliente cliente) {
        if (cliente == null) {
            LOGGER.log(Level.WARNING, "insertarCliente: cliente nulo.");
            return null;
        }
        // SQL de inserción
        String sql = "INSERT INTO clientes (nombre_completo, direccion, doc_identidad, telefono, ruc) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, cliente.getNombre_completo());
            pst.setString(2, cliente.getDireccion());
            pst.setString(3, cliente.getDoc_identidad());
            pst.setString(4, cliente.getTelefono());
            pst.setString(5, cliente.getRuc());

            int affected = pst.executeUpdate();
            if (affected == 0) {
                LOGGER.log(Level.WARNING, "insertarCliente: no se insertó ningún registro.");
                return null;
            }

            try (ResultSet rsKeys = pst.getGeneratedKeys()) {
                if (rsKeys.next()) {
                    int id = rsKeys.getInt(1);
                    cliente.setId_cliente(id);
                    LOGGER.log(Level.INFO, "Cliente insertado con id: {0}", id);
                    return cliente;
                } else {
                    LOGGER.log(Level.WARNING, "insertarCliente: no se obtuvo key generada.");
                    return null;
                }
            }

        } catch (SQLIntegrityConstraintViolationException ex) {
            // Violación de constraint única (duplicado) — mapear a log claro.
            LOGGER.log(Level.WARNING, "insertarCliente: violación de constraint (posible duplicado): {0}", ex.getMessage());
            LOGGER.log(Level.FINE, "Detalle", ex);
            return null;
        } catch (SQLException ex) {
            // Error general de BD
            LOGGER.log(Level.SEVERE, "Error insertarCliente: {0}", ex.toString());
            LOGGER.log(Level.FINE, "Detalle", ex);
            return null;
        }
    }

}
