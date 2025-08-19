package util.validation;

import dao.ClienteDAO;
import modelo.Cliente;
import java.util.regex.Pattern;

/**
 * Valida los campos de Cliente. Devuelve ValidationResult (OK o mensaje).
 * Reutilizable desde Controller o desde otros sitios.
 */
public class ClienteValidator {

    private final ClienteDAO dao;

    public ClienteValidator() {
        this.dao = new ClienteDAO();
    }

    // patrones
    private static final Pattern DNI_PATTERN = Pattern.compile("^\\d{8}$");
    private static final Pattern RUC_PATTERN = Pattern.compile("^\\d{11}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L}0-9\\.\\-/ ]+$");
    private static final Pattern ADDR_PATTERN = Pattern.compile("^[\\p{L}0-9\\.\\-/ ]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{9}$");

    // límites
    private static final int NAME_MIN = 3;
    private static final int NAME_MAX = 150;
    private static final int ADDR_MIN = 10;
    private static final int ADDR_MAX = 200;

    public ValidationResult validateForType(Cliente c, String tipo) {
        if (c == null) {
            return ValidationResult.fail("Cliente nulo.");
        }

        // nombre
        String nombre = safeTrim(c.getNombre_completo());
        if (nombre.isEmpty()) {
            return ValidationResult.fail("Nombre completo es obligatorio.");
        }
        if (nombre.length() < NAME_MIN) {
            return ValidationResult.fail("Nombre demasiado corto (mín " + NAME_MIN + " caracteres).");
        }
        if (nombre.length() > NAME_MAX) {
            return ValidationResult.fail("Nombre demasiado largo (máx " + NAME_MAX + " caracteres).");
        }
        if (!NAME_PATTERN.matcher(nombre).matches()) {
            return ValidationResult.fail("Nombre contiene caracteres inválidos. Sólo letras, números, espacios y - . /");
        }
        if (dao.existsByNombre(nombre)) {
            return ValidationResult.fail("El nombre ya está registrado.");
        }

        // direccion
        String direccion = safeTrim(c.getDireccion());
        if (direccion.isEmpty()) {
            return ValidationResult.fail("Dirección es obligatoria.");
        }
        if (direccion.length() < ADDR_MIN) {
            return ValidationResult.fail("Dirección demasiado corta (mín " + ADDR_MIN + " caracteres).");
        }
        if (direccion.length() > ADDR_MAX) {
            return ValidationResult.fail("Dirección demasiado larga (máx " + ADDR_MAX + " caracteres).");
        }
        if (!ADDR_PATTERN.matcher(direccion).matches()) {
            return ValidationResult.fail("Dirección contiene caracteres inválidos. Sólo letras, números, espacios y - . /");
        }
        if (dao.existsByDireccion(direccion)) {
            return ValidationResult.fail("La dirección ya está registrada.");
        }

        // tipo
        if ("Persona".equalsIgnoreCase(tipo)) {
            String doc = safeTrim(c.getDoc_identidad());
            if (!DNI_PATTERN.matcher(doc).matches()) {
                return ValidationResult.fail("DNI inválido. Debe tener 8 dígitos numéricos.");
            }
            if (dao.existsByDoc(doc)) {
                return ValidationResult.fail("El DNI ya está registrado.");
            }

            // teléfono opcional
            String tel = safeTrim(c.getTelefono());
            if (!tel.isEmpty()) {
                if (!PHONE_PATTERN.matcher(tel).matches()) {
                    return ValidationResult.fail("Teléfono inválido. Debe tener 9 dígitos numéricos.");
                }
                if (dao.existsByTelefono(tel)) {
                    return ValidationResult.fail("El teléfono ya está registrado.");
                }
            }
        } else if ("Empresa".equalsIgnoreCase(tipo)) {
            String ruc = safeTrim(c.getRuc());
            if (!RUC_PATTERN.matcher(ruc).matches()) {
                return ValidationResult.fail("RUC inválido. Debe tener 11 dígitos numéricos.");
            }
            if (dao.existsByRuc(ruc)) {
                return ValidationResult.fail("El RUC ya está registrado.");
            }

            // teléfono opcional
            String tel = safeTrim(c.getTelefono());
            if (!tel.isEmpty()) {
                if (!PHONE_PATTERN.matcher(tel).matches()) {
                    return ValidationResult.fail("Teléfono inválido. Debe tener 9 dígitos numéricos.");
                }
                if (dao.existsByTelefono(tel)) {
                    return ValidationResult.fail("El teléfono ya está registrado.");
                }
            }
        } else {
            return ValidationResult.fail("Tipo de cliente inválido.");
        }

        return ValidationResult.ok();
    }

    private String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }
}
