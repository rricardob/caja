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
    
    PermisosDAO permisosDAO = new PermisosDAO();
    
    // Método para obtener un cliente por su ID
    public Usuario login(String userName, String passwordHash) {
        System.out.println("Usuario "+ userName+ "pass "+passwordHash);
        String sql = "select "
                + "u.id_usuario, "
                + "u.nombre_usuario, "
                + "u.nombre_completo, "
                + "r.id_rol, "
                + "r.nombre as nombre_rol,"
                + "u.fecha_creacion "
                + "from usuarios u "
                + "INNER JOIN roles r ON u.id_rol = r.id_rol " 
                + "where u.nombre_usuario = ? and u.contraseña = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userName);
            stmt.setString(2, passwordHash);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                
                int idUsuario = rs.getInt("u.id_usuario");
                String nombreCompleto = rs.getString("u.nombre_completo");
                int idRol = rs.getInt("r.id_rol");
                String nombreRol = rs.getString("r.nombre_rol");
                
                // Iniciar sesión
                SessionManager session = SessionManager.getInstance();
                session.iniciarSesion(idUsuario, nombreCompleto, idRol, nombreRol);
                
                // Cargar permisos
                Set<String> permisos = permisosDAO.obtenerPermisosPorRol(idRol);
                session.setPermisos(permisos);

                return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre_usuario"),
                        "",
                        rs.getString("nombre_completo")
                );
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
