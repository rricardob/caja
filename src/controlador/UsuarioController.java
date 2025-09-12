package controlador;

import dao.UsuarioDAO;
import modelo.SessionManager;
import modelo.Usuario;


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
}
