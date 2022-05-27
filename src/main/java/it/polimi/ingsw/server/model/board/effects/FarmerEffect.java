package it.polimi.ingsw.server.model.board.effects;

import it.polimi.ingsw.server.model.player.Player;
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

    private final Map<HouseColor, Player> stolenProfessors;

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

    @Override
    public int getCost() {
        return 2;
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
