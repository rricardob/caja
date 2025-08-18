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
     * Validadores
     */
    public boolean existsByNombre(String nombre) {
        String sql = "SELECT 1 FROM clientes WHERE nombre_completo = ? LIMIT 1";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, nombre);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "existsByNombre: {0}", e.getMessage());
            return false;
        }
    }

    public boolean existsByDireccion(String direccion) {
        if (direccion == null || direccion.isEmpty()) {
            return false;
        }
        String sql = "SELECT 1 FROM clientes WHERE direccion = ? LIMIT 1";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, direccion);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "existsByDireccion: {0}", e.getMessage());
            return false;
        }
    }

    public boolean existsByDoc(String doc) {
        if (doc == null || doc.isEmpty()) {
            return false;
        }
        String sql = "SELECT 1 FROM clientes WHERE doc_identidad = ? LIMIT 1";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, doc);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "existsByDoc: {0}", e.getMessage());
            return false;
        }
    }

    public boolean existsByRuc(String ruc) {
        if (ruc == null || ruc.isEmpty()) {
            return false;
        }
        String sql = "SELECT 1 FROM clientes WHERE ruc = ? LIMIT 1";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, ruc);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "existsByRuc: {0}", e.getMessage());
            return false;
        }
    }

    public boolean existsByTelefono(String tel) {
        if (tel == null || tel.isEmpty()) {
            return false;
        }
        String sql = "SELECT 1 FROM clientes WHERE telefono = ? LIMIT 1";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, tel);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "existsByTelefono: {0}", e.getMessage());
            return false;
        }
    }

    /**
     * Busca un cliente por su indentificador (doc o ruc)
     */
    public Cliente buscarPorIdentificador(String identificador) {
        if (identificador == null || identificador.trim().isEmpty()) {
            return null;
        }
        String sql = "SELECT id_cliente, nombre_completo, direccion, doc_identidad, telefono, ruc FROM clientes WHERE doc_identidad = ? OR ruc = ? LIMIT 1";
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
            LOGGER.log(Level.SEVERE, "Error buscarPorIdentificador: {0}", e.getMessage());
        }
        return null;
    }

    /**
     * Inserta un nuevo cliente y devuelve el Cliente con id asignado (o null en
     * error)
     */
    public Cliente insertarCliente(Cliente cliente) {
        // Validaciones simples a nivel DAO (además de las de controller)
        if (existsByNombre(cliente.getNombre_completo())) {
            LOGGER.log(Level.WARNING, "insertarCliente: nombre ya existe: {0}", cliente.getNombre_completo());
            return null;
        }
        if (existsByDireccion(cliente.getDireccion())) {
            LOGGER.log(Level.WARNING, "insertarCliente: direccion ya existe: {0}", cliente.getDireccion());
            return null;
        }
        if (cliente.getDoc_identidad() != null && !cliente.getDoc_identidad().isEmpty() && existsByDoc(cliente.getDoc_identidad())) {
            LOGGER.log(Level.WARNING, "insertarCliente: doc ya existe: {0}", cliente.getDoc_identidad());
            return null;
        }
        if (cliente.getRuc() != null && !cliente.getRuc().isEmpty() && existsByRuc(cliente.getRuc())) {
            LOGGER.log(Level.WARNING, "insertarCliente: ruc ya existe: {0}", cliente.getRuc());
            return null;
        }
        if (cliente.getTelefono() != null && !cliente.getTelefono().isEmpty() && existsByTelefono(cliente.getTelefono())) {
            LOGGER.log(Level.WARNING, "insertarCliente: telefono ya existe: {0}", cliente.getTelefono());
            return null;
        }

        String sql = "INSERT INTO clientes (nombre_completo, direccion, doc_identidad, telefono, ruc) VALUES (?, ?, ?, ?, ?)";
        ResultSet rsKeys = null;
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, cliente.getNombre_completo());
            pst.setString(2, cliente.getDireccion());
            pst.setString(3, cliente.getDoc_identidad());
            pst.setString(4, cliente.getTelefono());
            pst.setString(5, cliente.getRuc());
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
            }
        } catch (SQLException e) {
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
        return null;
    }

}
