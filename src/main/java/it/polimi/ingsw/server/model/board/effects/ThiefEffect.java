package it.polimi.ingsw.server.model.board.effects;

/**
 * Specific effect n.12
 *
 * @author Riccardo Milici
 */

public class ThiefEffect extends Effect {

    /**
     * Class constructor.
     * It creates an instance of the class.
     */
    public ThiefEffect() {
    }

    @Override
    public int getId() {
        return 12;
    }

    @Override
    public void effect() {

    }

    @Override
    public int getCost() {
        return 3;
    }
}