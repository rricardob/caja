package controlador;

import dao.TipoTransaccionDAO;
import modelo.TipoTransaccion;
import util.validation.TipoTransaccionValidator;
import util.validation.ValidationResult;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TipoTransaccionController {

    private static final Logger LOGGER = Logger.getLogger(TipoTransaccionController.class.getName());

    private final TipoTransaccionDAO dao = new TipoTransaccionDAO();
    private final TipoTransaccionValidator validator = new TipoTransaccionValidator();

    // Validación para creación / uso genérico
    public ValidationResult validate(TipoTransaccion t) {
        try {
            return validator.validate(t);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Excepción en validate", ex);
            return ValidationResult.fail("Error inesperado en validación.");
        }
    }

    // Validación para edición (ignora unicidad contra el mismo id)
    public ValidationResult validate(TipoTransaccion t, Long existingId) {
        try {
            return validator.validate(t, existingId);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Excepción en validate(existingId)", ex);
            return ValidationResult.fail("Error inesperado en validación.");
        }
    }

    public TipoTransaccion crear(TipoTransaccion t) {
        ValidationResult r = validator.validate(t);
        if (!r.isOk()) {
            LOGGER.log(Level.INFO, "Validación fallida al crear tipo_transacciones: {0}", r.getMessage());
            throw new IllegalArgumentException(r.getMessage());
        }
        TipoTransaccion creado = dao.insertar(t);
        if (creado == null) {
            LOGGER.log(Level.WARNING, "insertar devolvió null para tipo_transacciones: {0}", t);
        } else {
            LOGGER.log(Level.INFO, "TipoTransaccion creado con id: {0}", creado.getId_tipo());
        }
        return creado;
    }

    public boolean actualizar(TipoTransaccion t) {
        if (t == null || t.getId_tipo() <= 0) {
            throw new IllegalArgumentException("Tipo de transacción inválido para actualizar.");
        }
        ValidationResult r = validator.validate(t, t.getId_tipo());
        if (!r.isOk()) {
            throw new IllegalArgumentException(r.getMessage());
        }
        boolean ok = dao.actualizar(t);
        if (!ok) {
            LOGGER.log(Level.WARNING, "DAO no actualizó tipo_transacciones: {0}", t);
        }
        return ok;
    }

    public boolean eliminar(long id) {
        try {
            return dao.eliminarPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error eliminar tipo_transacciones", ex);
            return false;
        }
    }

    public List<TipoTransaccion> listarTodos() {
        try {
            List<TipoTransaccion> all = dao.listarTodos();
            return (all == null) ? Collections.emptyList() : all;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error listar tipo_transacciones", ex);
            return Collections.emptyList();
        }
    }

    public TipoTransaccion buscarPorId(long id) {
        return dao.buscarPorId(id);
    }

    public TipoTransaccion buscarPorDescripcion(String descripcion) {
        return dao.buscarPorDescripcion(descripcion);
    }

}
