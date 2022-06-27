package it.polimi.ingsw.client.model;

/**
 * This class represents the Assistant (cards between 1 and 10) on the client side.
 *
 * @author Matteo Negro
 */
public class Assistant {
    private final int id;
    private final int maxDistance;
    private boolean bonus;

    /**
     * Class constructor.
     *
     * @param id    Identifier of the Assistant.
     * @param bonus Player bonus on the max distance.
     * @throws IndexOutOfBoundsException If the passed ID is not between 1 and 10.
     */
    public Assistant(int id, boolean bonus) {
        this.id = id;
        this.maxDistance = (id - 1) / 2 + 1;
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
     * Gets the maximum travel distance for Mother Nature.
     *
     * @return Maximum travel distance for Mother Nature
     */
    public int getMaxDistance() {
        return maxDistance + (bonus ? 2 : 0);
    }

    /**
     * Gets the bonus second.
     *
     * @return The bonus.
     */
    public boolean hasBonus() {
        return this.bonus;
    }

    /**
     * Sets the bonus to true.
     */
    public void setBonus() {
        this.bonus = true;
    }
}
