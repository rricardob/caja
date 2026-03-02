package controlador;

import dao.ChequeDAO;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Cheque;
import modelo.SessionManager;

public class ChequeController {

    private static final Logger LOGGER = Logger.getLogger(ChequeController.class.getName());
    private final ChequeDAO chequeDAO;
    private final SessionManager session;

    public ChequeController() {
        this.chequeDAO = new ChequeDAO();
        this.session = SessionManager.getInstance();
    }

    /**
     * Registra un nuevo cheque verificando la sesión activa.
     */
    public int guardarCheque(String nroCheque, java.util.Date fechaEmision, String saldoAnteriorStr,
            String totalChequeStr) {
        if (!session.sesionActiva()) {
            throw new IllegalStateException("Debe iniciar sesión para realizar esta operación.");
        }

        if (nroCheque == null || nroCheque.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de cheque es obligatorio.");
        }

        if (fechaEmision == null) {
            throw new IllegalArgumentException("La fecha de emisión es obligatoria.");
        }

        try {
            BigDecimal saldoAnterior = new BigDecimal(saldoAnteriorStr.replace(",", "."));
            BigDecimal totalCheque = new BigDecimal(totalChequeStr.replace(",", "."));

            if (totalCheque.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El total del cheque debe ser mayor a cero.");
            }

            Cheque cheque = new Cheque(
                    (long) session.getIdUsuario(),
                    nroCheque,
                    new java.sql.Date(fechaEmision.getTime()),
                    saldoAnterior,
                    totalCheque);

            return chequeDAO.guardar(cheque);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Los importes deben ser números válidos.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error en ChequeController.guardarCheque: " + e.getMessage(), e);
            return -1;
        }
    }

    /**
     * Obtiene la lista de todos los cheques.
     */
    public List<Cheque> listarCheques() {
        return chequeDAO.listar();
    }

    /**
     * Obtiene la lista de cheques filtrada.
     */
    public List<Cheque> listarCheques(java.util.Date inicio, java.util.Date fin, String nroCheque) {
        java.sql.Date sqlInicio = (inicio != null) ? new java.sql.Date(inicio.getTime()) : null;
        java.sql.Date sqlFin = (fin != null) ? new java.sql.Date(fin.getTime()) : null;
        return chequeDAO.listarConFiltros(sqlInicio, sqlFin, nroCheque);
    }

    /**
     * Actualiza un cheque existente con validaciones.
     */
    public boolean actualizarCheque(Cheque cheque) {
        if (!session.sesionActiva()) {
            throw new IllegalStateException("Debe iniciar sesión para realizar esta operación.");
        }

        if (cheque.getNro_cheque() == null || cheque.getNro_cheque().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de cheque es obligatorio.");
        }

        if (cheque.getFecha_emision() == null) {
            throw new IllegalArgumentException("La fecha de emisión es obligatoria.");
        }

        if (cheque.getTotal_cheque() == null || cheque.getTotal_cheque().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El total del cheque debe ser mayor a cero.");
        }

        return chequeDAO.actualizar(cheque);
    }

    /**
     * Elimina un cheque por su ID.
     */
    public boolean eliminarCheque(long idCheque) {
        if (!session.sesionActiva()) {
            throw new IllegalStateException("Debe iniciar sesión para realizar esta operación.");
        }
        return chequeDAO.eliminar(idCheque);
    }
}
