package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when an illegal action is performed during the game.
 *
 * @author Riccardo Motta
 */
public class IllegalActionException extends Exception {

    /**
     * Default exception constructor.
     */
    public IllegalActionException() {
        super();
    }

    /**
     * Default exception constructor.
     *
     * @param message The message to display.
     */
    public IllegalActionException(String message) {
        super(message);
    }
}
