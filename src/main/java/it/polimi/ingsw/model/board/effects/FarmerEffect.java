package it.polimi.ingsw.model.board.effects;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.utilities.HouseColor;

import java.util.HashMap;
import java.util.Map;

/**
 * Specific effect n.2
 *
 * @author Riccardo Milici
 */

public class FarmerEffect extends Effect {

    private Map<HouseColor, Player> stolenProfessors;

    /**
     * Class constructor.
     * It creates an instance of the class containing an empty map of the professors stolen from players.
     */
    public FarmerEffect() {
        stolenProfessors = new HashMap<>();
    }

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public void effect() {

    }

    /**
     * effect() method overload.
     * Calls the stealProfessors(Map<HouseColor, Player> stolen) private method.
     * @param stolen
     */
    public void effect(Map<HouseColor, Player> stolen) {
        stealProfessors(stolen);
    }

    @Override
    public void clean() {
        returnProfessors();
    }

    @Override
    public int getCost() {
        return 2;
    }

    /**
     * Returns the Map saved in the stolenProfessors attribute.
     * @return stolenProfessors attribute.
     */
    private Map<HouseColor, Player> getStolenProfessors() {
        return stolenProfessors;
    }

    /**
     * Saves the input map, containing a mapping between the color of the professors stolen from the players to whom they belong, into the stolenProfessors attribute.
     * @param stolen
     */
    private void stealProfessors(Map<HouseColor, Player> stolen) {
        stolenProfessors = stolen;
    }

    /**
     * Deletes the mapping defined in the stolenProfessor attribute, creating a new HashMap.
     */
    private void returnProfessors() {
        stolenProfessors = new HashMap<>();
    }
}
