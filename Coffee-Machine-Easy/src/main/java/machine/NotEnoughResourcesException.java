package machine;

/**
 * This exception is thrown when the coffee machine tries to make a coffee but does not have enough resources
 */
public class NotEnoughResourcesException extends Exception {

    public NotEnoughResourcesException(String message) {
        super(message);
    }
}
