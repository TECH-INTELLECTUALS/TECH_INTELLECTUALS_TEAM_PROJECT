package exceptions;

/**
 * Thrown when a request is invalid or malformed.
 */
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }
}
