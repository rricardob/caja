package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;


public class UsuarioController {
    
    private UsuarioDAO usuarioDAO;

    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }
    
    public Usuario login(String nickname, String password){
        return this.usuarioDAO.login(nickname, password);
    }
    
}
