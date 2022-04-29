package it.polimi.ingsw.model.board.effects;

import it.polimi.ingsw.model.board.Island;

import java.util.ArrayList;
import java.util.List;

/**
 * Specific effect n.6
 *
 * @author Riccardo Milici
 */

public class CentaurEffect extends Effect {

    /**
     * Class constructor.
     * It creates an instance of the class.
     */
    public CentaurEffect() {
    }

    /**
     * Class constructor used to restore the game.
     *
     * @param statusTakenTowers A map containing the towers taken from their respective
     */
    public CentaurEffect(List<Island> statusTakenTowers) {
    }

    @Override
    public int getId() {
        return 6;
    }

    @Override
    public void effect() {

    }

    @Override
    public void clean() {
    }

    @Override
    public int getCost() {
        return 3;
    }
}
