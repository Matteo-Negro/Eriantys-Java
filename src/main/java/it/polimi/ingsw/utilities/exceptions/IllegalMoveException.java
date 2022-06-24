package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when an illegal move is performed during the game.
 *
 * @author Matteo Negro
 */
public class IllegalMoveException extends Exception {

    /**
     * Default exception constructor.
     */
    public IllegalMoveException() {
        super();
    }

    /**
     * Default exception constructor.
     *
     * @param message The message to display.
     */
    public IllegalMoveException(String message) {
        super(message);
    }
}
