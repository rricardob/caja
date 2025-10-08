package dao;

import modelo.CategoriaTransaccion;
import util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoriaTransaccionDAO {

    private static final Logger LOGGER = Logger.getLogger(CategoriaTransaccionDAO.class.getName());

    public List<CategoriaTransaccion> listarTodos() {
        List<CategoriaTransaccion> lista = new ArrayList<>();
        String sql = "SELECT id_categoria_transacciones, descripcion FROM categoria_transacciones ORDER BY descripcion ASC";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                CategoriaTransaccion c = new CategoriaTransaccion(
                        rs.getLong("id_categoria_transacciones"),
                        rs.getString("descripcion")
                );
                lista.add(c);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error listarTodos categoria_transacciones", ex);
        }
        return lista;
    }

}
