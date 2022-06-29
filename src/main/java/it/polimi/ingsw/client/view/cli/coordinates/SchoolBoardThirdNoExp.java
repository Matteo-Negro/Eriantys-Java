package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * SchoolBoard: third position, no exp.
 */
public class SchoolBoardThirdNoExp implements DeltaCoordinates {

    private static SchoolBoardThirdNoExp instance = null;

    private SchoolBoardThirdNoExp() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SchoolBoardThirdNoExp getInstance() {
        if (instance == null) instance = new SchoolBoardThirdNoExp();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 14;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 15;
    }
}
