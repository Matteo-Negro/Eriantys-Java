package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when a wrong game code is used to search for a game.
 *
 * @author Riccardo Milici
 */
public class GameNotFoundException extends Exception{

    public GameNotFoundException() {
        super();
    }

    public GameNotFoundException(String message) {
        super(message);
    }
}
