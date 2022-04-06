package it.polimi.ingsw.model.utilities.exceptions;

/**
 * Exception thrown when something has already been played.
 *
 * @author Riccardo Motta
 */
public class AlreadyPlayedException extends Exception {
    public AlreadyPlayedException() {
        super();
    }

    public AlreadyPlayedException(String message) {
        super(message);
    }
}
