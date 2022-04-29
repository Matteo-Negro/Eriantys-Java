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

    /**
     * Class constructor used to restore the game.
     *
     * @param statusAvailableBans
     */
    public HerablistEffect(int statusAvailableBans) {
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
     * @param command       The action to be performed.
     */
    public void effect(String command) {
        switch (command){
            case "take" -> takeBan();
            case "restore" -> restoreBan();
        }

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
}
