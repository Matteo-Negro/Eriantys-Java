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

    private List<Island> takenTowers;

    /**
     * Class constructor.
     * It creates an instance of the class containing an ArrayList of the islands from which the towers will be taken.
     */
    public CentaurEffect() {
        takenTowers = new ArrayList<Island>();
    }

    /**
     * Class constructor used to restore the game.
     * @param statusTakenTowers     A map containing the towers taken from their respective
     */
    public CentaurEffect(List<Island> statusTakenTowers) {
        this.takenTowers = statusTakenTowers;
    }

    @Override
    public int getId() {
        return 6;
    }

    @Override
    public void effect() {

    }

    /**
     * effect() method's overload.
     * Calls the takeTowers(List<Island> towers) private method.
     *
     * @param towers
     */
    public void effect(List<Island> towers) {
        takeTowers(towers);
    }

    @Override
    public void clean() {
        returnTowers();
    }

    @Override
    public int getCost() {
        return 3;
    }

    /**
     * Returns the takenTowers (List<Island>) attribute.
     *
     * @return takenTowers attribute.
     */
    private List<Island> getTakenTowers() {
        return takenTowers;
    }

    /**
     * Saves the list of islands, from which the towers are being taken, in the takenTowers attribute.
     *
     * @param towers
     */
    private void takeTowers(List<Island> towers) {
        takenTowers = towers;
    }

    /**
     * Deletes the list of islands saved in the takenTowers attribute, creating a new ArrayList.
     */
    private void returnTowers() {
        takenTowers = new ArrayList<Island>();
    }
}
