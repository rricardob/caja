package controlador;

import dao.ReposicionDAO;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Reposicion;
import util.validation.ReposicionValidator;
import util.validation.ValidationResult;

public class ReposicionController {

    private static final Logger LOGGER = Logger.getLogger(ReposicionController.class.getName());

    private final ReposicionDAO reposicionDAO = new ReposicionDAO();

    public Reposicion crearReposicion(int idtransaccion) {
        Reposicion reposicion = reposicionDAO.insertarReposicion(idtransaccion);
        if (reposicion == null) {
            LOGGER.log(Level.WARNING, "insertarCliente devolvió null para cliente: {0}", reposicion);
        } else {
            LOGGER.log(Level.INFO, "Cliente creado con id: {0}", reposicion.getId_reposicionamiento());
        }
        return reposicion;
    }
    
    public ValidationResult validateReposicion(String descripcion, String importeStr) {
        try {
            return ReposicionValidator.validate(descripcion, importeStr);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Excepción en validateResposicion", ex);
            return ValidationResult.fail("Error inesperado en validación del reposicion.");
        }
    }

}
