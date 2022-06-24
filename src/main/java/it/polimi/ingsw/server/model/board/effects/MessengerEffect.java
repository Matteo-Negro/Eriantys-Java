package it.polimi.ingsw.server.model.board.effects;

/**
 * Specific effect n.4.
 *
 * @author Riccardo Milici
 */
public class MessengerEffect implements Effect {

    /**
     * Returns the identification number of the specific effect object.
     *
     * @return id attribute.
     */
    @Override
    public int getId() {
        return 4;
    }

    /**
     * Returns the activation cost of the effect.
     *
     * @return cost attribute.
     */
    @Override
    public int getCost() {
        return 1;
    }
}
