package exceptions;

/**
 * Thrown when a requested resource is not available.
 */
public class ResourceUnavailableException extends RuntimeException {

    public ResourceUnavailableException(String message) {
        super(message);
    }
}
