package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when a second is negative.
 *
 * @author Riccardo Motta
 */
public class NegativeException extends Exception {

    /**
     * Default exception constructor.
     */
    public NegativeException() {
        super();
    }

    /**
     * Default exception constructor.
     *
     * @param message The message to display.
     */
    public NegativeException(String message) {
        super(message);
    }
}
