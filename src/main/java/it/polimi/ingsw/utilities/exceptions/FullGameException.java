package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when someone tries to add a new player to the game, but there already enough players.
 *
 * @author Riccardo Motta
 */
public class FullGameException extends Exception {

    /**
     * Default exception constructor.
     */
    public FullGameException() {
        super();
    }

    /**
     * Default exception constructor.
     *
     * @param message The message to display.
     */
    public FullGameException(String message) {
        super(message);
    }
}
