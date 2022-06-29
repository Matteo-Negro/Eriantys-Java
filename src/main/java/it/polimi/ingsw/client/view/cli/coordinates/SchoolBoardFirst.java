package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * SchoolBoard: first position.
 */
public class SchoolBoardFirst implements DeltaCoordinates {

    private static SchoolBoardFirst instance = null;

    private SchoolBoardFirst() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SchoolBoardFirst getInstance() {
        if (instance == null) instance = new SchoolBoardFirst();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 120;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 2;
    }
}
