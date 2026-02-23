
package controlador;

import dao.RegistroBancoDAO;
import java.util.List;
import modelo.Banco;

public class RegistroBancoController {
    
    private final RegistroBancoDAO registroBancoDAO;

    public RegistroBancoController() {
        this.registroBancoDAO = new RegistroBancoDAO();
    }
    
    public Integer registrarBanco(Banco banco) {
        return this.registroBancoDAO.registrarBanco(banco);
    }
    
    public boolean actualizarBanco(Banco banco){
        return this.registroBancoDAO.actualizarBanco(banco);
    }
    
    public boolean eliminarBanco(long idBanco){
        return this.registroBancoDAO.eliminarBanco(idBanco);
    }
    
    public List<Banco> listarBanco(String descripcion) {
        return this.registroBancoDAO.obtenerBancos(descripcion);
    }
    
    public Banco obtenerBancoPorId(long idBanco){
        return this.registroBancoDAO.obteneBancoPorId(idBanco);
    }
    
    public boolean existeBanco(String nombreBanco){
        return this.registroBancoDAO.existeBanco(nombreBanco);
    }
    
}
