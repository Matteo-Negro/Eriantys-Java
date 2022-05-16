package it.polimi.ingsw.server.model.board.effects;

import java.util.Objects;

/**
 * Specific effect n.5
 *
 * @author Riccardo Milici
 */

public class HerbalistEffect extends Effect {

    private int availableBans;

    /**
     * Class constructor.
     * It creates an instance of the class containing a maximum number of possible bans for the islands.
     */
    public HerbalistEffect() {
        availableBans = 5;
    }

    /**
     * Class constructor used to restore the game.
     *
     * @param statusAvailableBans Indicates the bans that are still available, saved into the status. These are going to be stored into the availableBans attribute.
     */
    public HerbalistEffect(int statusAvailableBans) {
        this.availableBans = statusAvailableBans;
    }

    @Override
    public int getId() {
        return 5;
    }

    @Override
    public void effect() {

    }

    /**
     * effect() method overload.
     * Decides to call the takeBan() method or the restoreBan() method, through a conditional branch on command parameter.
     *
     * @param command The action to be performed.
     */
    public void effect(String command) {
        switch (command) {
            case "take" -> takeBan();
            case "restore" -> restoreBan();
        }

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
    public int getAvailableBans() {
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
    private void restoreBan() {
        availableBans++;
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
        HerbalistEffect that = (HerbalistEffect) o;
        return availableBans == that.availableBans;
    }

    /**
     * Calculates the hash.
     *
     * @return The calculated hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(availableBans);
    }
}
