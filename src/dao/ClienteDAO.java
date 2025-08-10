package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Cliente;
import util.ConexionDB;

public class ClienteDAO {

    private static final Logger LOGGER = Logger.getLogger(ClienteDAO.class.getName());

    /**
     * Busca un cliente por su documento de identidad
     */
    public Cliente buscarPorDoc(String docIdentidad) {
        String sql = "SELECT id_cliente, nombre_completo, direccion, doc_identidad FROM clientes WHERE doc_identidad = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, docIdentidad);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new Cliente(
                            rs.getInt("id_cliente"),
                            rs.getString("nombre_completo"),
                            rs.getString("direccion"),
                            rs.getString("doc_identidad")
                    );
                    return c;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error buscarPorDoc: {0}", e.getMessage());
        }
        return null;
    }

    /**
     * Inserta un nuevo cliente y devuelve el Cliente con id asignado (o null en
     * error)
     */
    public Cliente insertarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre_completo, direccion, doc_identidad) VALUES (?, ?, ?)";
        ResultSet rsKeys = null;
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, cliente.getNombre_completo());
            pst.setString(2, cliente.getDireccion());
            pst.setString(3, cliente.getDoc_identidad());
            int affected = pst.executeUpdate();
            if (affected == 0) {
                LOGGER.log(Level.WARNING, "insertarCliente: no se insertó ningun registro.");
                return null;
            }
            rsKeys = pst.getGeneratedKeys();
            if (rsKeys.next()) {
                int id = rsKeys.getInt(1);
                cliente.setId_cliente(id);
                LOGGER.log(Level.INFO, "Cliente insertado con id: {0}", id);
                return cliente;
            } else {
                LOGGER.log(Level.WARNING, "insertarCliente: no se obtuvo clave generada.");
                return null;
            }
        } catch (SQLException e) {
            // Manejo de violación de constraint (doc_identidad único)
            LOGGER.log(Level.SEVERE, "Error insertarCliente: {0}", e.getMessage());
            return null;
        } finally {
            try {
                if (rsKeys != null) {
                    rsKeys.close();
                }
            } catch (SQLException ex) {
            }
        }
    }
}
