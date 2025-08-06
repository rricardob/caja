
package controlador;

import dao.CajaDAO;

public class CajaController {
    
    private final CajaDAO cajaDAO;
    
    public CajaController(){
        this.cajaDAO = new CajaDAO();
    }
    
    public boolean verificarEstadoCaja(){
        return this.cajaDAO.verificarEstadoCaja();
    }
    
}
