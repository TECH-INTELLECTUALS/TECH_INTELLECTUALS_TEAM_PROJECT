package exceptions;

/**
 * Thrown when a duplicate record is detected during persistence.
 */
public class DuplicateRecordException extends RuntimeException {

    public DuplicateRecordException(String message) {
        super(message);
    }
}
