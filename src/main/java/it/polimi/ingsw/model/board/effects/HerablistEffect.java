package it.polimi.ingsw.model.board.effects;

/**
 * Specific effect n.5
 *
 * @author Riccardo Milici
 */

public class HerablistEffect extends Effect {

    private int availableBans;

    /**
     * Class constructor.
     * It creates an instance of the class containing a maximum number of possible bans for the islands.
     */
    public HerablistEffect() {
        availableBans = 5;
    }

    @Override
    public int getId() {
        return 5;
    }

    @Override
    public void effect() {
        takeBan();
    }

    @Override
    public void clean() {

    }

    @Override
    public int getCost() {
        return 2;
    }

    /**
     * Returns the availableBans attribute.
     *
     * @return availableBans attribute.
     */
    private int getAvailableBans() {
        return availableBans;
    }

    /**
     * Decreases the number of the available bans.
     */
    private void takeBan() {
        availableBans--;
    }

    /**
     * Increases the number of the available bans.
     */
    private void putBan() {
        availableBans++;
    }
}
