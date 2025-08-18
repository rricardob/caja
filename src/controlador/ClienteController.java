package controlador;

import dao.ClienteDAO;
import modelo.Cliente;
import java.util.regex.Pattern;

public class ClienteController {

    private final ClienteDAO dao = new ClienteDAO();

    private static final Pattern DNI_PATTERN = Pattern.compile("^\\d{8}$");
    private static final Pattern RUC_PATTERN = Pattern.compile("^\\d{11}$");

    /**
     * Valida los datos del cliente según tipo ("Persona" o "Empresa u
     * Organización"). Retorna null si todo OK, o un mensaje de error si falla.
     */
    public String validarCliente(Cliente c, String tipo) {
        if (c.getNombre_completo() == null || c.getNombre_completo().trim().isEmpty()) {
            return "Nombre completo es obligatorio.";
        }
        if (c.getDireccion() == null || c.getDireccion().trim().isEmpty()) {
            return "Dirección es obligatoria.";
        }
        if (dao.existsByNombre(c.getNombre_completo())) {
            return "El nombre ya está registrado.";
        }
        if (dao.existsByDireccion(c.getDireccion())) {
            return "La dirección ya está registrada.";
        }

        if ("Persona".equalsIgnoreCase(tipo)) {
            String doc = c.getDoc_identidad();
            if (doc == null || !DNI_PATTERN.matcher(doc).matches()) {
                return "DNI inválido. Debe tener 8 dígitos numéricos.";
            }
            if (dao.existsByDoc(doc)) {
                return "El DNI ya está registrado.";
            }

            if (c.getTelefono() != null && !c.getTelefono().isEmpty() && dao.existsByTelefono(c.getTelefono())) {
                return "El teléfono ya está registrado.";
            }
        } else if ("Empresa".equalsIgnoreCase(tipo)) {
            String ruc = c.getRuc();
            if (ruc == null || !RUC_PATTERN.matcher(ruc).matches()) {
                return "RUC inválido. Debe tener 11 dígitos numéricos.";
            }
            if (dao.existsByRuc(ruc)) {
                return "El RUC ya está registrado.";
            }
            if (c.getTelefono() != null && !c.getTelefono().isEmpty() && dao.existsByTelefono(c.getTelefono())) {
                return "El teléfono ya está registrado.";
            }
        } else {
            return "Tipo de cliente inválido.";
        }

        return null; // OK
    }

    /**
     * Crea el cliente si pasa validación; retorna Cliente con id o null si
     * falla. Devuelve null también si la DAO no pudo insertar.
     */
    public Cliente crearCliente(Cliente c, String tipo) {
        String v = validarCliente(c, tipo);
        if (v != null) {
            throw new IllegalArgumentException(v);
        }
        return dao.insertarCliente(c);
    }

    public Cliente buscarPorIdentificador(String id) {
        return dao.buscarPorIdentificador(id);
    }

}
