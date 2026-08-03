package exceptions;

/**
 * Thrown when no route can be found between campus locations.
 */
public class RouteNotFoundException extends RuntimeException {

    public RouteNotFoundException(String message) {
        super(message);
    }
}
