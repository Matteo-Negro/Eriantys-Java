package it.polimi.ingsw.server.model.board.effects;

/**
 * Specific effect n.2.
 *
 * @author Riccardo Milici
 */
public class FarmerEffect implements Effect {

    /**
     * Returns the identification number of the specific effect object.
     *
     * @return id attribute.
     */
    @Override
    public int getId() {
        return 2;
    }

    /**
     * Returns the activation cost of the effect.
     *
     * @return cost attribute.
     */
    @Override
    public int getCost() {
        return 2;
    }
}
