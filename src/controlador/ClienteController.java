package controlador;

import dao.ClienteDAO;
import modelo.Cliente;
import util.validation.ClienteValidator;
import util.validation.ValidationResult;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ClienteController {

    private static final Logger LOGGER = Logger.getLogger(ClienteController.class.getName());

    private final ClienteDAO dao = new ClienteDAO();
    private final ClienteValidator validator = new ClienteValidator();

    /**
     * Método que expone la validación sin lanzar excepción. Útil para la UI.
     */
    public ValidationResult validateForType(Cliente c, String tipo) {
        try {
            return validator.validateForType(c, tipo);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Excepción en validateForType", ex);
            return ValidationResult.fail("Error inesperado en validación.");
        }
    }

    /**
     * Crea un cliente: valida mediante ClienteValidator y si está OK llama a
     * DAO. Lanza IllegalArgumentException con mensaje de validación si falla.
     */
    public Cliente crearCliente(Cliente c, String tipo) {
        ValidationResult r = validator.validateForType(c, tipo);
        if (!r.isOk()) {
            LOGGER.log(Level.INFO, "Validación fallida al crear cliente: {0}", r.getMessage());
            throw new IllegalArgumentException(r.getMessage());
        }
        Cliente creado = dao.insertarCliente(c);
        if (creado == null) {
            LOGGER.log(Level.WARNING, "insertarCliente devolvió null para cliente: {0}", c);
        } else {
            LOGGER.log(Level.INFO, "Cliente creado con id: {0}", creado.getId_cliente());
        }
        return creado;
    }

    /**
     * Busca cliente por identificador (dni o ruc)
     */
    public Cliente buscarPorIdentificador(String id) {
        return dao.buscarPorIdentificador(id);
    }

    /**
     * Retorna la lista completa de clientes (o vacía en error).
     */
    public List<Cliente> listarClientes() {
        try {
            return dao.listarTodos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error listarClientes", ex);
            return Collections.emptyList();
        }
    }

    /**
     * Actualiza un cliente: valida y luego llama a DAO. Retorna true si
     * actualizó. Lanza IllegalArgumentException si la validación falla.
     */
    public boolean actualizarCliente(Cliente c, String tipo) {
        // validar con validator (reutiliza validateForType)
        ValidationResult r = validator.validateForType(c, tipo);
        if (!r.isOk()) {
            throw new IllegalArgumentException(r.getMessage());
        }
        boolean ok = dao.actualizarCliente(c);
        if (!ok) {
            LOGGER.log(Level.WARNING, "DAO no actualizó cliente: {0}", c);
        }
        return ok;
    }

    /**
     * Elimina cliente por id. Retorna true si OK.
     */
    public boolean eliminarCliente(int idCliente) {
        try {
            return dao.eliminarClientePorId(idCliente);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error eliminarCliente", ex);
            return false;
        }
    }

}
