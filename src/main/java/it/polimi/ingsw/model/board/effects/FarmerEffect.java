package it.polimi.ingsw.model.board.effects;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utilities.HouseColor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    /**
     * Class constructor used to restore the game.
     *
     * @param statusStolenProfessors
     */
    public FarmerEffect(Map<HouseColor, Player> statusStolenProfessors) {
        this.stolenProfessors = statusStolenProfessors;
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
     *
     * @param stolen
     */
    public void effect(Map<HouseColor, Player> stolen) {
        stealProfessors(stolen);
    }

    @Override
    public int getCost() {
        return 2;
    }

    /**
     * Returns the Map saved in the stolenProfessors attribute.
     *
     * @return stolenProfessors attribute.
     */
    public Map<HouseColor, Player> getStolenProfessors() {
        Map<HouseColor, Player> professors = new HashMap<>(this.stolenProfessors);
        stolenProfessors = new HashMap<>();
        return professors;
    }

    /**
     * Saves the input map, containing a mapping between the color of the professors stolen from the players to whom they belong, into the stolenProfessors attribute.
     *
     * @param stolen
     */
    private void stealProfessors(Map<HouseColor, Player> stolen) {
        stolenProfessors = stolen;
    }

    /**
     * Standard redefinition of "equals" method.
     *
     * @param o Object to compare.
     * @return true if the two objects are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FarmerEffect that = (FarmerEffect) o;
        return Objects.equals(stolenProfessors, that.stolenProfessors);
    }

    /**
     * Calculates the hash.
     *
     * @return The calculated hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(stolenProfessors);
    }
}
