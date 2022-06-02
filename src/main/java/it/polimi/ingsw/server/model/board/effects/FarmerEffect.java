package it.polimi.ingsw.server.model.board.effects;

/**
 * Specific effect n.2
 *
 * @author Riccardo Milici
 */

public class FarmerEffect implements Effect {

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public int getCost() {
        return 2;
    }
}
