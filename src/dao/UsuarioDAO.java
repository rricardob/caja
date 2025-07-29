package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.Usuario;
import util.ConexionDB;


public class UsuarioDAO {

    // Método para obtener un cliente por su ID
    public Usuario login(String userName, String passwordHash) {
        String sql = "select user_id, username, password_hash, full_name, role_id, created_at "
                + "from users where username = ? and password_hash = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userName);
            stmt.setString(2, passwordHash);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                return new Usuario(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        "",
                        rs.getString("fullname")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
