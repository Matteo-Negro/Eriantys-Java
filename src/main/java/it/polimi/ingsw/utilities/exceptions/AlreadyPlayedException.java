package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when something has already been played.
 *
 * @author Riccardo Motta
 */
public class AlreadyPlayedException extends Exception {

    /**
     * Default exception constructor.
     */
    public AlreadyPlayedException() {
        super();
    }

    /**
     * Default exception constructor.
     *
     * @param message The message to display.
     */
    public AlreadyPlayedException(String message) {
        super(message);
    }
}
