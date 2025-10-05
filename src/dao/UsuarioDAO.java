package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import modelo.SessionManager;
import modelo.Usuario;
import util.ConexionDB;

public class UsuarioDAO {

    private final PermisosDAO permisosDAO = new PermisosDAO();

    public Usuario login(String nombre_usuario, String clave_usuario) {
        String sql = "SELECT u.id_usuario, u.nombre_usuario, u.nombre_completo, "
                + "r.id_rol, r.nombre AS nombre_rol, u.fecha_creacion, u.fecha_actualizacion "
                + "FROM usuario u "
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
                        nombre_completo,
                        0,
                        null,
                        null
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Usuario infoUsuario(Integer idUsuario) {
        String sql = "select id_usuario, nombre_usuario, nombre_completo from usuario where id_usuario = ?";
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
                        nombre_completo,
                        0,
                        null,
                        null
                );
            }
        } catch (SQLException ex) {
            System.err.println("Error al infoUsuario: " + ex.getMessage());
        }
        return null;
    }

    public Integer registrarUsuario(Usuario usuario, int rol) {
        String sql = "insert into usuario (nombre_usuario, clave_usuario, nombre_completo, id_rol) values "
                + "(?,?,?,?)";

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNombre_usuario());
            stmt.setString(2, usuario.getClave_usuario());
            stmt.setString(3, usuario.getNombre_completo());
            stmt.setInt(4, rol);

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al registrarUsuario: " + ex.getMessage());
        }
        return null;
    }

    public List<Usuario> obtenerUsuarios(String nombreUsuario) {
        StringBuilder sqlBuilder = new StringBuilder("select * from usuario");

        boolean agregarWhereUsuario = (nombreUsuario != null && !nombreUsuario.trim().isEmpty());

        if (agregarWhereUsuario) {
            sqlBuilder.append(" WHERE (nombre_usuario LIKE ?)");  // Solo agregamos si hay parámetro
        }

        //sqlBuilder.append(" ORDER BY nombre_usuario DESC");  // Ordenamiento al final
        List<Usuario> sesiones = new ArrayList<>();

        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {

            int paramIndex = 1;

            if (agregarWhereUsuario) {
                String param = "%" + nombreUsuario + "%";  // Coincidencia en cualquier parte
                stmt.setString(paramIndex++, param);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sesiones.add(mapearResultSet(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtener usuarios: " + ex.getMessage());
        }
        return sesiones;
    }

    /**
     * Mapear ResultSet a objeto Usuario
     */
    private Usuario mapearResultSet(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId_usuario(rs.getInt("id_usuario"));
        usuario.setNombre_usuario(rs.getString("nombre_usuario"));
        usuario.setClave_usuario(rs.getString("clave_usuario"));
        usuario.setNombre_completo(rs.getString("nombre_completo"));
        usuario.setId_rol(rs.getInt("id_rol"));
        usuario.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
        usuario.setFecha_actualizacion(rs.getTimestamp("fecha_actualizacion"));
        return usuario;
    }

    public boolean actualizarUsuario(Usuario usuario) {

        if (usuario == null || usuario.getId_usuario() <= 0) {
            return false;
        }
        String sql = "update usuario set nombre_usuario = ?, clave_usuario = ?, nombre_completo = ? where id_usuario = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, usuario.getNombre_usuario());
            pst.setString(2, usuario.getClave_usuario());
            pst.setString(3, usuario.getNombre_completo());
            pst.setInt(4, usuario.getId_usuario());
            return pst.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException ex) {
            // duplicado u otra restriccion
            System.err.println("actualizarUsuario: violación de constraint: " + ex.getMessage());
            return false;
        } catch (SQLException ex) {
            System.err.println("Error actualizarCliente" + ex);
            return false;
        }

    }

    public boolean eliminarUsuario(int usuario) {
        if (usuario <= 0) {
            return false;
        }
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, usuario);
            return pst.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException ex) {
            System.err.println("eliminarUsuario: violación de constraint: " + ex.getMessage());
            return false;
        } catch (SQLException ex) {
            System.err.println("Error eliminarUsuario: " + ex.getMessage());
            return false;
        }
    }

}
