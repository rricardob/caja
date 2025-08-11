
package controlador;

import dao.CajaDAO;
import java.sql.Timestamp;

public class CajaController {
    
    private final CajaDAO cajaDAO;
    
    public CajaController(){
        this.cajaDAO = new CajaDAO();
    }
    
    public boolean verificarEstadoCaja(){
        return this.cajaDAO.verificarEstadoCaja();
    }
    
    public int actualizarCaja(Integer usuario_id, Timestamp fecha_inicio, Timestamp fecha_fin, Double saldo_inicio, Double saldo_final, String estado){
        return this.cajaDAO.actualizarCaja(usuario_id, fecha_inicio, fecha_fin, saldo_inicio, saldo_final, estado);
    }
}
