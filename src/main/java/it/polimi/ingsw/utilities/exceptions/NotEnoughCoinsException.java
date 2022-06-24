package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when there are not enough coins for the specified cost.
 *
 * @author Riccardo Motta
 */
public class NotEnoughCoinsException extends Exception {

    /**
     * Default exception constructor.
     */
    public NotEnoughCoinsException() {
        super();
    }

    /**
     * Default exception constructor.
     *
     * @param message The message to display.
     */
    public NotEnoughCoinsException(String message) {
        super(message);
    }
}
