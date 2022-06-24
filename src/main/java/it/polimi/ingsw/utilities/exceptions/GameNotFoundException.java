package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when a wrong game code is used to search for a game.
 *
 * @author Riccardo Milici
 */
public class GameNotFoundException extends Exception {

    /**
     * Default exception constructor.
     */
    public GameNotFoundException() {
        super();
    }

    /**
     * Default exception constructor.
     *
     * @param message The message to display.
     */
    public GameNotFoundException(String message) {
        super(message);
    }
}
