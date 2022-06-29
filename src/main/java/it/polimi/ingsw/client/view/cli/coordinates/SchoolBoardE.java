package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * SchoolBoard: E movement.
 */
public class SchoolBoardE implements DeltaCoordinates {

    private static SchoolBoardE instance = null;

    private SchoolBoardE() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SchoolBoardE getInstance() {
        if (instance == null) instance = new SchoolBoardE();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 29;
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
