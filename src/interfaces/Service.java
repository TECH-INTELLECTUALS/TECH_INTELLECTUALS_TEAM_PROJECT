package interfaces;

/**
 * Common service contract for business service classes.
 */
public interface Service {

    /**
     * Starts the service.
     */
    void start();

    /**
     * Stops the service.
     */
    void stop();
}
