package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when the current round is concluded.
 *
 * @author Riccardo Motta
 */
public class RoundConcluded extends Exception {
    public RoundConcluded() {
        super();
    }

    public RoundConcluded(String message) {
        super(message);
    }
}
