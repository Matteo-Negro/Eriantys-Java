package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Draw options: cursor position.
 */
public class DrawOptions implements DeltaCoordinates {

    private static DrawOptions instance = null;

    private DrawOptions() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static DrawOptions getInstance() {
        if (instance == null)
            instance = new DrawOptions();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 2;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 10;
    }
}
