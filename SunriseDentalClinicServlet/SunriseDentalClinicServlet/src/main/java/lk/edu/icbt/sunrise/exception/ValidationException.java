package lk.edu.icbt.sunrise.exception;

import java.util.Map;

public class ValidationException extends RuntimeException {
    private final Map<String, String> errors;

    public ValidationException(Map<String, String> e) {
        super("Validation failed");
        errors = Map.copyOf(e);
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
