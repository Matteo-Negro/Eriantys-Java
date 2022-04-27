package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when someone tries to create a player whose name is not uniq.
 *
 * @author Riccardo Motta
 */
public class IslandNotFoundException extends Exception {
    public IslandNotFoundException() {
        super();
    }

    public IslandNotFoundException(String message) {
        super(message);
    }
}
