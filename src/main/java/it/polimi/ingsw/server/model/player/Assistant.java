package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.utilities.Log;

import java.util.Objects;

/**
 * This class represents the Assistant (cards between 1 and 10) have to be played to define the turn order.
 *
 * @author Riccardo Motta
 */
public class Assistant {

    private final int id;
    private final int maxDistance;
    private boolean bonus;

    /**
     * Class constructor.
     *
     * @param id Identifier of the Assistant.
     * @throws IndexOutOfBoundsException If the passed ID is not between 1 and 10.
     */
    public Assistant(int id) throws IndexOutOfBoundsException {
        if (id < 1 || id > 10)
            throw new IndexOutOfBoundsException("The Assistant has to have a second between 1 and 10, passed " + id + ".");
        this.id = id;
        this.maxDistance = (id - 1) / 2 + 1;
        this.bonus = false;

        Log.info("*** New Assistant successfully created with id: " + id);
    }

    /**
     * Class constructor used to restore the game.
     *
     * @param id    Identifier of the Assistant.
     * @param bonus true if the assistant has a movement bonus.
     * @throws IndexOutOfBoundsException If the passed ID is not between 1 and 10.
     */
    public Assistant(int id, boolean bonus) throws IndexOutOfBoundsException {
        if (id < 1 || id > 10)
            throw new IndexOutOfBoundsException("The Assistant has to have a second between 1 and 10, passed " + id + ".");
        this.id = id;
        this.maxDistance = id / 2 + 1;
        this.bonus = bonus;
    }

    /**
     * Gets the ID of the card.
     *
     * @return ID of the card.
     */
    public int getId() {
        return id;
    }

    /**
     * Tells if the card has a bonus.
     *
     * @return Bonus boolean attribute.
     */
    public boolean hasBonus() {
        return bonus;
    }

    /**
     * Gets the maximum travel distance for Mother Nature.
     *
     * @return Maximum travel distance for Mother Nature
     */
    public int getMaxDistance() {
        return maxDistance + (bonus ? 2 : 0);
    }

    /**
     * Sets the bonus to true.
     */
    public void setBonus() {
        bonus = true;
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
        Assistant assistant = (Assistant) o;
        return id == assistant.id && maxDistance == assistant.maxDistance && bonus == assistant.bonus;
    }

    /**
     * Calculates the hash.
     *
     * @return The calculated hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, maxDistance, bonus);
    }
}
