package controlador;

import dao.CajaDAO;
import dao.ClienteDAO;
import dao.TransaccionDAO;
import modelo.Cliente;
import modelo.SessionManager;
import java.math.BigDecimal;
import java.util.List;
import modelo.Transaccion;

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
        return clienteDAO.buscarPorIdentificador(doc);
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

    /**
     * Lista los ingresos de la sesión activa (retorna lista vacía si no hay
     * sesión o error).
     */
    public List<Transaccion> listarIngresosPorSesionActiva() {
        int idUsuario = session.getIdUsuario();
        int idSesion = cajaDAO.obtenerIdSesionActivaPorUsuario(idUsuario);
        if (idSesion == -1) {
            return java.util.Collections.emptyList();
        }
        return transaccionDAO.listarIngresosPorSesion(idSesion);
    }

    /**
     * Obtiene la transacción por id (incluyendo datos de cliente) o null si no
     * existe/error.
     */
    public Transaccion obtenerTransaccionPorId(int idTrans) {
        return transaccionDAO.obtenerTransaccionPorId(idTrans);
    }

}
