
package util.validation;

import java.math.BigDecimal;

public final class ReposicionValidator {

    public ReposicionValidator() {
    }
    
    public static ValidationResult validate(String descripcion, String importeStr) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return ValidationResult.fail("Descripción es obligatoria.");
        }
        String descTrim = descripcion.trim();
        if (descTrim.length() < 5) {
            return ValidationResult.fail("Descripción demasiado corta (mín 5 caracteres).");
        }
        if (descTrim.length() > 200) {
            return ValidationResult.fail("Descripción demasiado larga (máx 200 caracteres).");
        }

        if (importeStr == null || importeStr.trim().isEmpty()) {
            return ValidationResult.fail("Importe es obligatorio.");
        }
        String impTrim = importeStr.trim();

        // permitir coma o punto como separador decimal; normalizar a punto para BigDecimal
        String normalized = impTrim.replace(',', '.');

        // validar formato: dígitos, opcionalmente separador y decimales
        if (!normalized.matches("^\\d+(\\.\\d+)?$")) {
            return ValidationResult.fail("Importe inválido. Sólo números o decimales con punto/coma.");
        }
        try {
            BigDecimal importe = new BigDecimal(normalized);
            if (importe.compareTo(BigDecimal.ONE) < 0) {
                return ValidationResult.fail("El importe debe ser como mínimo 1.");
            }
        } catch (NumberFormatException ex) {
            return ValidationResult.fail("Importe inválido.");
        }

        return ValidationResult.ok();
    }
    
}
