package util.validation;

import dao.TipoTransaccionDAO;
import modelo.TipoTransaccion;
import java.util.regex.Pattern;

public class TipoTransaccionValidator {

    private final TipoTransaccionDAO dao;

    public TipoTransaccionValidator() {
        this.dao = new TipoTransaccionDAO();
    }

    private static final int DESC_MIN = 5;
    private static final int DESC_MAX = 100;
    // Reusar el mismo conjunto de caracteres permitido en Cliente (letras, números, espacios y - . /)
    private static final Pattern DESC_PATTERN = Pattern.compile("^[\\p{L}0-9\\./\\- ]+$");

    public ValidationResult validate(TipoTransaccion t) {
        return validate(t, null);
    }

    public ValidationResult validate(TipoTransaccion t, Long existingId) {
        if (t == null) {
            return ValidationResult.fail("Tipo de transacción nulo.");
        }

        // descripcion
        String descripcion = safeTrim(t.getDescripcion());
        if (descripcion.isEmpty()) {
            return ValidationResult.fail("Descripción es obligatoria.");
        }
        if (descripcion.length() < DESC_MIN) {
            return ValidationResult.fail("Descripción demasiado corta (mín 5 caracteres).");
        }
        if (descripcion.length() > DESC_MAX) {
            return ValidationResult.fail("Descripción demasiado larga (máx 100 caracteres).");
        }
        if (!DESC_PATTERN.matcher(descripcion).matches()) {
            return ValidationResult.fail("Descripción contiene caracteres inválidos. Sólo letras, números, espacios y - . /");
        }
        boolean descExists = (existingId == null)
                ? dao.existsByDescripcion(descripcion)
                : dao.existsByDescripcionExceptId(descripcion, existingId);
        if (descExists) {
            return ValidationResult.fail("La descripción ya está registrada.");
        }

        // estado
        if (t.getEstado() == null) {
            return ValidationResult.fail("Estado es obligatorio.");
        }

        // categoria (FK)
        Long idCat = t.getId_categoria_transacciones();
        if (idCat == null) {
            return ValidationResult.fail("Categoría de transacciones es obligatoria.");
        }
        if (idCat <= 0) {
            return ValidationResult.fail("Categoría de transacciones inválida.");
        }
        if (!dao.existsCategoriaById(idCat)) {
            return ValidationResult.fail("La categoría de transacciones no existe.");
        }

        return ValidationResult.ok();
    }

    private String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }

}
