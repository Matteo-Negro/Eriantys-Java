package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * SchoolBoard: cursor reset in case of 2 players.
 */
public class SchoolBoardReset2Player implements DeltaCoordinates {

    private static SchoolBoardReset2Player instance = null;

    private SchoolBoardReset2Player() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SchoolBoardReset2Player getInstance() {
        if (instance == null) instance = new SchoolBoardReset2Player();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -29;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 0;
    }
}
