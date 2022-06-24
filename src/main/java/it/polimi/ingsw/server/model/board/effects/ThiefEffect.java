package it.polimi.ingsw.server.model.board.effects;

/**
 * Specific effect n.12.
 *
 * @author Riccardo Milici
 */
public class ThiefEffect implements Effect {

    /**
     * Returns the identification number of the specific effect object.
     *
     * @return id attribute.
     */
    @Override
    public int getId() {
        return 12;
    }

    /**
     * Returns the activation cost of the effect.
     *
     * @return cost attribute.
     */
    @Override
    public int getCost() {
        return 3;
    }
}
