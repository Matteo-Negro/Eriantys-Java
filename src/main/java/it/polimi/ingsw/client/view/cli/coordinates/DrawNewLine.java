package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Draw page: new line movement.
 */
public class DrawNewLine implements DeltaCoordinates {

    private static DrawNewLine instance = null;

    private DrawNewLine() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static DrawNewLine getInstance() {
        if (instance == null)
            instance = new DrawNewLine();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -38;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 1;
    }
}
