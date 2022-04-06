package it.polimi.ingsw.model.player;

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
    Assistant(int id) throws IndexOutOfBoundsException {
        if (id < 1 || id > 10)
            throw new IndexOutOfBoundsException("The Assistant has to have a value between 1 and 10, passed " + id + ".");
        this.id = id;
        this.maxDistance = id / 2 + 1;
        this.bonus = false;
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
     * Sets the bonus to true.
     */
    public void setBonus() {
        bonus = true;
    }
}