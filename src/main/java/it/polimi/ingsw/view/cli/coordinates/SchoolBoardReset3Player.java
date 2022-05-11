package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

/**
 * SchoolBoard: cursor reset in case of 3 players.
 */
public class SchoolBoardReset3Player implements DeltaCoordinates {

    private static SchoolBoardReset3Player instance = null;

    private SchoolBoardReset3Player() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SchoolBoardReset3Player getInstance() {
        if (instance == null) instance = new SchoolBoardReset3Player();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -14;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return -15;
    }
}
