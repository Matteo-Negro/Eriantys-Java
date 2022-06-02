package it.polimi.ingsw.server.model.board.effects;

import it.polimi.ingsw.utilities.exceptions.NoMoreBansLeftException;

import java.util.Objects;

/**
 * Specific effect n.5
 *
 * @author Riccardo Milici
 */

public class HerbalistEffect implements Effect {

    private int availableBans;

    /**
     * Class constructor.
     * It creates an instance of the class containing a maximum number of possible bans for the islands.
     */
    public HerbalistEffect() {
        availableBans = 4;
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

    /**
     * effect() method overload.
     * Decides to call the takeBan() method or the restoreBan() method, through a conditional branch on command parameter.
     *
     * @param action The action to be performed.
     */
    public void effect(Action action) throws NoMoreBansLeftException {
        if (action.equals(Action.TAKE))
            takeBan();
        else
            restoreBan();
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
    private void takeBan() throws NoMoreBansLeftException {
        if (availableBans == 0)
            throw new NoMoreBansLeftException();
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

    public enum Action {TAKE, RESTORE}
}
