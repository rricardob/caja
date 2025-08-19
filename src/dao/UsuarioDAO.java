package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import modelo.SessionManager;
import modelo.Usuario;
import util.ConexionDB;

public class UsuarioDAO {

    private final PermisosDAO permisosDAO = new PermisosDAO();

    public Usuario login(String nombre_usuario, String clave_usuario) {
        String sql = "SELECT u.id_usuario, u.nombre_usuario, u.nombre_completo, "
                + "r.id_rol, r.nombre AS nombre_rol, u.fecha_creacion, u.fecha_actualizacion "
                + "FROM usuarios u "
                + "INNER JOIN roles r ON u.id_rol = r.id_rol "
                + "WHERE u.nombre_usuario = ? AND u.clave_usuario = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre_usuario);
            stmt.setString(2, clave_usuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id_usuario = rs.getInt("id_usuario");
                String nombre_completo = rs.getString("nombre_completo");
                int id_rol = rs.getInt("id_rol");
                String nombre_rol = rs.getString("nombre_rol");

                // Iniciar sesión
                SessionManager session = SessionManager.getInstance();
                session.iniciarSesion(id_usuario, nombre_usuario, id_rol, nombre_rol);

                // Cargar permisos
                Set<String> permisos = permisosDAO.obtenerPermisosPorRol(id_rol);
                session.setPermisos(permisos);

                return new Usuario(
                        id_usuario,
                        nombre_usuario,
                        "",
                        nombre_completo
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Usuario infoUsuario(Integer idUsuario) {
        String sql = "select id_usuario, nombre_usuario, nombre_completo from usuarios where id_usuario = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id_usuario = rs.getInt("id_usuario");
                String nombre_usuario = rs.getString("nombre_usuario");
                String nombre_completo = rs.getString("nombre_completo");

                return new Usuario(
                        id_usuario,
                        nombre_usuario,
                        "",
                        nombre_completo
                );
            }
        } catch (SQLException ex) {
            System.err.println("Error al infoUsuario: " + ex.getMessage());
        }
        return null;
    }
}
