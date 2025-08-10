package controlador;

import dao.CajaDAO;
import dao.ClienteDAO;
import dao.TransaccionDAO;
import modelo.Cliente;
import modelo.SessionManager;
import java.math.BigDecimal;

public class IngresoController {

    private final ClienteDAO clienteDAO;
    private final CajaDAO cajaDAO;
    private final TransaccionDAO transaccionDAO;
    private final SessionManager session;

    public IngresoController() {
        this.clienteDAO = new ClienteDAO();
        this.cajaDAO = new CajaDAO();
        this.transaccionDAO = new TransaccionDAO();
        this.session = SessionManager.getInstance();
    }

    public Cliente buscarClientePorDoc(String doc) {
        return clienteDAO.buscarPorDoc(doc);
    }

    /**
     * Guarda un ingreso y devuelve id_transaccion o -1 en error.
     */
    public int guardarIngreso(Cliente cliente, BigDecimal importe, String descripcion) {
        if (cliente == null) {
            return -1;
        }
        if (!session.sesionActiva()) {
            return -1;
        }

        int idUsuario = session.getIdUsuario();
        int idSesion = cajaDAO.obtenerIdSesionActivaPorUsuario(idUsuario);
        if (idSesion == -1) {
            return -1; // No hay sesión activa
        }
        return transaccionDAO.guardarIngreso(idSesion, idUsuario, cliente.getId_cliente(), importe, descripcion);
    }
}
