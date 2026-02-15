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

public class IngresoController {

    private static final Logger LOGGER = Logger.getLogger(IngresoController.class.getName());

    private final ClienteDAO clienteDAO;
    private final SesionCajaDAO cajaDAO;
    private final TransaccionDAO transaccionDAO;
    private final SessionManager session;

    // Nombre del cliente estático de reposiciones
    private static final String REPOSICION_CLIENT_NAME = "USUARIO_REPOSICION";

    public IngresoController() {
        this.clienteDAO = new ClienteDAO();
        this.cajaDAO = new SesionCajaDAO();
        this.transaccionDAO = new TransaccionDAO();
        this.session = SessionManager.getInstance();
    }

    public Cliente buscarClientePorDoc(String doc) {
        return clienteDAO.buscarPorIdentificador(doc);
    }

    /**
     * Método que expone la validación del formulario (sin lanzar excepciones).
     * La UI puede usarlo para mostrar mensajes antes de llamar a
     * guardarIngreso.
     */
    public ValidationResult validateIngreso(String doc, String descripcion, String importeStr) {
        try {
            return IngresoValidator.validate(doc, descripcion, importeStr);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Excepción en validateIngreso", ex);
            return ValidationResult.fail("Error inesperado en validación del ingreso.");
        }
    }

    /**
     * Guarda un ingreso y devuelve id_transaccion o -1 en error. Lanza
     * IllegalArgumentException para errores de validación de parámetros,
     * IllegalStateException si no hay sesión activa.
     */
    public int guardarIngreso(Cliente cliente, BigDecimal importe, String descripcion, long idTipo) {
        // validaciones mínimas de negocio
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
            return transaccionDAO.guardarIngreso(idSesion, idUsuario, cliente.getId_cliente(), importe, descripcion,
                    idTipo);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error en transaccionDAO.guardarIngreso", ex);
            return -1;
        }
    }

    /**
     * Actualiza una transacción existente (importe, descripción, tipo).
     */
    public boolean actualizarTransaccion(Transaccion t) {
        if (t == null || t.getId_transaccion() <= 0) {
            throw new IllegalArgumentException("Transacción inválida para actualizar.");
        }

        // Validar importe y descripción
        if (t.getImporte() == null || t.getImporte().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El importe debe ser mayor que 0.");
        }

        if (t.getDescripcion() == null || t.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria.");
        }

        if (t.getId_tipo() <= 0) {
            throw new IllegalArgumentException("Tipo de transacción inválido.");
        }

        try {
            boolean ok = transaccionDAO.actualizarTransaccion(t);
            if (!ok) {
                LOGGER.log(Level.WARNING, "DAO no actualizó transacción: {0}", t.getId_transaccion());
            }
            return ok;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error actualizar transacción", ex);
            return false;
        }
    }

    /**
     * Elimina una transacción por su ID.
     */
    public boolean eliminarTransaccion(int idTransaccion) {
        try {
            return transaccionDAO.eliminarTransaccion(idTransaccion);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error eliminar transacción", ex);
            return false;
        }
    }

    /**
     * Lista los ingresos de la sesión activa (retorna lista vacía si no hay
     * sesión o error).
     */
    public List<Transaccion> listarIngresosPorSesionActiva() {
        try {
            if (!session.sesionActiva()) {
                return java.util.Collections.emptyList();
            }
            int idUsuario = session.getIdUsuario();
            int idSesion = cajaDAO.obtenerIdSesionActivaPorUsuario(idUsuario);
            if (idSesion == -1) {
                return java.util.Collections.emptyList();
            }
            // Mostrar solo INGRESO
            return transaccionDAO.listarIngresosPorSesion(idSesion);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error listarIngresosPorSesionActiva", ex);
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Obtiene la transacción por id (incluyendo datos de cliente) o null si no
     * existe/error.
     */
    public Transaccion obtenerTransaccionPorId(int idTrans) {
        return transaccionDAO.obtenerTransaccionPorId(idTrans);
    }

    /**
     * Obtiene el historial de ingresos con filtros avanzados.
     */
    public List<Transaccion> obtenerHistorialIngresos(java.sql.Date fechaInicio, java.sql.Date fechaFin,
            String dniRuc, Long idTipo, java.math.BigDecimal montoMin, java.math.BigDecimal montoMax) {
        try {
            return transaccionDAO.listarIngresosPorRangoFechas(fechaInicio, fechaFin, dniRuc, idTipo, montoMin,
                    montoMax);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error obtenerHistorialIngresos avanzado", ex);
            return java.util.Collections.emptyList();
        }
    }

}
