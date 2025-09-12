package util.validation;

/**
 * Resultados de validación: OK o error con mensajes.
 */
public final class ValidationResult {

    private final boolean ok;
    private final String message;

    private ValidationResult(boolean ok, String message) {
        this.ok = ok;
        this.message = message;
    }

    public static ValidationResult ok() {
        return new ValidationResult(true, null);
    }

    public static ValidationResult fail(String message) {
        return new ValidationResult(false, message);
    }

    public boolean isOk() {
        return ok;
    }

    public String getMessage() {
        return message;
    }
}
