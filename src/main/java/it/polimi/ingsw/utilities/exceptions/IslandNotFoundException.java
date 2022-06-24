package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when someone tries to create a player whose name is not unique.
 *
 * @author Riccardo Motta
 */
public class IslandNotFoundException extends Exception {

    /**
     * Default exception constructor.
     */
    public IslandNotFoundException() {
        super();
    }

    /**
     * Default exception constructor.
     *
     * @param message The message to display.
     */
    public IslandNotFoundException(String message) {
        super(message);
    }
}
