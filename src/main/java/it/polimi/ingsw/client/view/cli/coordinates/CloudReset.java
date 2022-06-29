package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Cloud: cursor reset.
 */
public class CloudReset implements DeltaCoordinates {

    private static CloudReset instance = null;

    private CloudReset() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static CloudReset getInstance() {
        if (instance == null)
            instance = new CloudReset();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -13;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return -7;
    }
}
