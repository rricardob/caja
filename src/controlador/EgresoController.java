package controlador;

import dao.SesionCajaDAO;
import dao.ClienteDAO;
import dao.TransaccionDAO;
import modelo.Cliente;
import modelo.SessionManager;
import modelo.Transaccion;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.validation.IngresoValidator;
import util.validation.ValidationResult;

public class EgresoController {

    private static final Logger LOGGER = Logger.getLogger(EgresoController.class.getName());

    private final ClienteDAO clienteDAO;
    private final SesionCajaDAO cajaDAO;
    private final TransaccionDAO transaccionDAO;
    private final SessionManager session;

    public EgresoController() {
        this.clienteDAO = new ClienteDAO();
        this.cajaDAO = new SesionCajaDAO();
        this.transaccionDAO = new TransaccionDAO();
        this.session = SessionManager.getInstance();
    }

    public Cliente buscarClientePorDoc(String doc) {
        return clienteDAO.buscarPorIdentificador(doc);
    }

    /**
     * Reutilizamos la validación de ingresos ya que los campos para egresos son los
     * mismos.
     */
    public ValidationResult validateEgreso(String doc, String descripcion, String importeStr) {
        try {
            return IngresoValidator.validate(doc, descripcion, importeStr);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Excepción en validateEgreso", ex);
            return ValidationResult.fail("Error inesperado en validación del egreso.");
        }
    }

    /**
     * Guarda un egreso. Lanza excepciones para errores de estado o negocio.
     */
    public int guardarEgreso(Cliente cliente, BigDecimal importe, String descripcion, long idTipo) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente requerido.");
        }
        if (importe == null) {
            throw new IllegalArgumentException("Importe inválido.");
        }
        if (importe.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El importe debe ser mayor que 0.");
        }
        if (idTipo <= 0) {
            throw new IllegalArgumentException("Tipo de transacción inválido.");
        }
        if (!session.sesionActiva()) {
            throw new IllegalStateException("No hay sesión de caja activa.");
        }

        int idUsuario = session.getIdUsuario();
        int idSesion = cajaDAO.obtenerIdSesionActivaPorUsuario(idUsuario);
        if (idSesion == -1) {
            throw new IllegalStateException("No hay ninguna sesión de caja abierta. Debe aperturar caja primero.");
        }

        try {
            // Reutilizamos el método de guardado genérico del DAO
            return transaccionDAO.guardarIngreso(idSesion, idUsuario, cliente.getId_cliente(), importe, descripcion,
                    idTipo);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error en transaccionDAO.guardarEgreso", ex);
            return -1;
        }
    }

    /**
     * Lista los egresos de la sesión activa.
     */
    public List<Transaccion> listarEgresosPorSesionActiva() {
        try {
            if (!session.sesionActiva()) {
                return java.util.Collections.emptyList();
            }
            int idUsuario = session.getIdUsuario();
            int idSesion = cajaDAO.obtenerIdSesionActivaPorUsuario(idUsuario);
            if (idSesion == -1) {
                return java.util.Collections.emptyList();
            }
            return transaccionDAO.listarEgresosPorSesion(idSesion);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error listarEgresosPorSesionActiva", ex);
            return java.util.Collections.emptyList();
        }
    }

    public boolean eliminarTransaccion(long idTransaccion) {
        try {
            return transaccionDAO.eliminarTransaccion((int) idTransaccion);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error eliminar transacción de egreso", ex);
            return false;
        }
    }

    public boolean actualizarTransaccion(Transaccion transaccion) {
        try {
            return transaccionDAO.actualizarTransaccion(transaccion);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error actualizar transacción de egreso", ex);
            return false;
        }
    }

    public Transaccion obtenerTransaccionPorId(int idTrans) {
        return transaccionDAO.obtenerTransaccionPorId(idTrans);
    }
}
