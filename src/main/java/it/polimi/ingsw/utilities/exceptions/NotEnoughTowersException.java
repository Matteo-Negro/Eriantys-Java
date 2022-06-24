package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when there are not enough towers.
 *
 * @author Riccardo Motta
 */
public class NotEnoughTowersException extends Exception {

    /**
     * Default exception constructor.
     */
    public NotEnoughTowersException() {
        super();
    }

    /**
     * Default exception constructor.
     *
     * @param message The message to display.
     */
    public NotEnoughTowersException(String message) {
        super(message);
    }
}
