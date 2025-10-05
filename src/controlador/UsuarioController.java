package controlador;

import dao.UsuarioDAO;
import java.util.List;
import modelo.SessionManager;
import modelo.Usuario;
import util.Roles;


public class UsuarioController {

    private UsuarioDAO usuarioDAO;
    private SessionManager session;

    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
        this.session = SessionManager.getInstance();
    }

    public Usuario login(String nombre_usuario, String clave_usuario) {
        return this.usuarioDAO.login(nombre_usuario, clave_usuario);
    }

    public void cerrarSesion() {
        this.session.cerrarSesion();
    }
    
    public List<Usuario> obtenerUsuarios(String nombreUsuario){
        return this.usuarioDAO.obtenerUsuarios(nombreUsuario);
    }
    
    public Integer registrarUsuario(Usuario usuario){
        return this.usuarioDAO.registrarUsuario(usuario, Roles.CAJERO.getId());
    }
    
    public boolean actualizarUsuario(Usuario usuario){
        return this.usuarioDAO.actualizarUsuario(usuario);
    }
    
    public boolean eliminarUsuario(int IdUsuario){
        return this.usuarioDAO.eliminarUsuario(IdUsuario);
    }
}
