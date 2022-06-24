package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when the current round is concluded.
 *
 * @author Riccardo Motta
 */
public class RoundConcluded extends Exception {

    /**
     * Default exception constructor.
     */
    public RoundConcluded() {
        super();
    }

    /**
     * Default exception constructor.
     *
     * @param message The message to display.
     */
    public RoundConcluded(String message) {
        super(message);
    }
}
