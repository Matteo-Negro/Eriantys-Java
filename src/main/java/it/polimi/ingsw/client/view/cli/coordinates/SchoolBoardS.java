package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * SchoolBoard: S movement.
 */
public class SchoolBoardS implements DeltaCoordinates {

    private static SchoolBoardS instance = null;

    private SchoolBoardS() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SchoolBoardS getInstance() {
        if (instance == null) instance = new SchoolBoardS();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 0;
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
