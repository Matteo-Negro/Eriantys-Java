package it.polimi.ingsw.model.utilities.exceptions;

/**
 * Exception thrown when there are not enough towers.
 *
 * @author Riccardo Motta
 */
public class NotEnoughTowersException extends Exception {
    public NotEnoughTowersException() {
        super();
    }

    public NotEnoughTowersException(String message) {
        super(message);
    }
}
