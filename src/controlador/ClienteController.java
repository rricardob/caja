package controlador;

import dao.ClienteDAO;
import modelo.Cliente;
import util.validation.ClienteValidator;
import util.validation.ValidationResult;

public class ClienteController {

    private final ClienteDAO dao = new ClienteDAO();
    private final ClienteValidator validator = new ClienteValidator();

    /**
     * Crea un cliente: valida mediante ClienteValidator y si está OK llama a
     * DAO. Lanza IllegalArgumentException con mensaje de validación si falla.
     */
    public Cliente crearCliente(Cliente c, String tipo) {
        ValidationResult r = validator.validateForType(c, tipo);
        if (!r.isOk()) {
            throw new IllegalArgumentException(r.getMessage());
        }
        return dao.insertarCliente(c);
    }

    /**
     * Busca cliente por identificador (dni o ruc)
     */
    public Cliente buscarPorIdentificador(String id) {
        return dao.buscarPorIdentificador(id);
    }

}
