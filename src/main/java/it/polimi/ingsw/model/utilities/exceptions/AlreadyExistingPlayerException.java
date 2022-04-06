package it.polimi.ingsw.model.utilities.exceptions;

/**
 * Exception thrown when someone tries to create a player whose name is not uniq.
 *
 * @author Riccardo Motta
 */
public class AlreadyExistingPlayerException extends Exception {
    public AlreadyExistingPlayerException() {
        super();
    }

    public AlreadyExistingPlayerException(String message) {
        super(message);
    }
}
