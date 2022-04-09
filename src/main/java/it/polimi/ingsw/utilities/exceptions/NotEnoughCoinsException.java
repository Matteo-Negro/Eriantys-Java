package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when there are not enough coins for the specified cost.
 *
 * @author Riccardo Motta
 */
public class NotEnoughCoinsException extends Exception {
    public NotEnoughCoinsException() {
        super();
    }

    public NotEnoughCoinsException(String message) {
        super(message);
    }
}
