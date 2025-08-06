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
    
    public Usuario login(String nickname, String password){
        return this.usuarioDAO.login(nickname, password);
    }
    
    public void cerrarSesion() {
        this.session.cerrarSesion();
    }
    
}
