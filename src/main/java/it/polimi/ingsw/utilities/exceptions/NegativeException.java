package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when a value is negative.
 *
 * @author Riccardo Motta
 */
public class NegativeException extends Exception {
    public NegativeException() {
        super();
    }

    public NegativeException(String message) {
        super(message);
    }
}
