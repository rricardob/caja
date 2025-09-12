package controlador;

import dao.ClienteDAO;
import java.util.ArrayList;
import modelo.Cliente;
import util.validation.ClienteValidator;
import util.validation.ValidationResult;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.Collections;

public class ClienteController {

    private static final Logger LOGGER = Logger.getLogger(ClienteController.class.getName());

    private final ClienteDAO dao = new ClienteDAO();
    private final ClienteValidator validator = new ClienteValidator();

    // Nombre del cliente "estático" que usamos para reposiciones.
    private static final String REPOSICION_CLIENT_NAME = "USUARIO_REPOSICION";

    /**
     * Método que expone la validación sin lanzar excepción. Útil para la UI.
     * (versión para creación / validación genérica)
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
     * Sobrecarga de la validación que acepta existingId: si existingId != null
     * las comprobaciones de unicidad ignorarán ese id (útil para edición).
     */
    public ValidationResult validateForType(Cliente c, String tipo, Integer existingId) {
        try {
            return validator.validateForType(c, tipo, existingId);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Excepción en validateForType(existingId)", ex);
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

    public Cliente buscarPorNombre(String nombre) {
        return dao.buscarPorNombre(nombre);
    }

    /**
     * Retorna la lista completa de clientes (o vacía en error).
     */
    public List<Cliente> listarClientes() {
        try {
            List<Cliente> all = dao.listarTodos();
            if (all == null || all.isEmpty()) {
                return Collections.emptyList();
            }

            // Intentamos obtener el cliente estático por nombre (si existe)
            Cliente repoClient = dao.buscarPorNombre(REPOSICION_CLIENT_NAME);
            Integer repoId = (repoClient != null) ? repoClient.getId_cliente() : null;

            // Filtrar lista en memoria (muy barato para listas no gigantescas)
            List<Cliente> filtered = new ArrayList<>(all.size());
            for (Cliente c : all) {
                if (c == null) {
                    continue;
                }
                // Excluir por id si encontramos registro; además excluir si nombre coincide por si acaso
                boolean isRepoById = (repoId != null && c.getId_cliente() == repoId);
                boolean isRepoByName = (c.getNombre_completo() != null && c.getNombre_completo().equalsIgnoreCase(REPOSICION_CLIENT_NAME));
                if (isRepoById || isRepoByName) {
                    // saltar - no agregar a filtered
                    continue;
                }
                filtered.add(c);
            }
            return filtered;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error listarClientes", ex);
            return Collections.emptyList();
        }
    }

    /**
     * Actualiza un cliente: valida (ignorando el mismo id para unicidad) y
     * luego llama a DAO. Retorna true si actualizó. Lanza
     * IllegalArgumentException si la validación falla.
     */
    public boolean actualizarCliente(Cliente c, String tipo) {
        if (c == null || c.getId_cliente() <= 0) {
            throw new IllegalArgumentException("Cliente inválido para actualizar.");
        }

        // Validar usando existingId = id del cliente (evita falsos positivos en unicidad)
        ValidationResult r = validator.validateForType(c, tipo, c.getId_cliente());
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
            LOGGER.log(Level.SEVERE, "Error eliminar Cliente", ex);
            return false;
        }
    }

}
