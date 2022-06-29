package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Options: cursor position.
 */
public class Options implements DeltaCoordinates {

    private static Options instance = null;

    private Options() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static Options getInstance() {
        if (instance == null)
            instance = new Options();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 3;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 11;
    }
}
