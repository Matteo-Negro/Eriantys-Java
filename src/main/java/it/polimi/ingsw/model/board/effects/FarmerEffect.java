package it.polimi.ingsw.model.board.effects;

/**
 * Specific effect n.2
 *
 * @author Riccardo Milici
 */

public class FarmerEffect extends Effect {

    /**
     * Class constructor.
     * It creates an instance of the class containing an empty map of the professors stolen from players.
     */
    public FarmerEffect() {
    }

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public void effect() {

    }

    @Override
    public int getCost() {
        return 2;
    }
}
