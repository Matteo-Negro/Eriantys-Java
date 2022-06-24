package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when someone tries to create a player whose name is not uniq.
 *
 * @author Riccardo Motta
 */
public class AlreadyExistingPlayerException extends Exception {

    /**
     * Default exception constructor.
     */
    public AlreadyExistingPlayerException() {
        super();
    }

    /**
     * Default exception constructor.
     *
     * @param message The message to display.
     */
    public AlreadyExistingPlayerException(String message) {
        super(message);
    }
}
