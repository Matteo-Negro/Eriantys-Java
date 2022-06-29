package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Waiting options: cursor position.
 */
public class WaitingOptions implements DeltaCoordinates {

    private static WaitingOptions instance = null;

    private WaitingOptions() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static WaitingOptions getInstance() {
        if (instance == null)
            instance = new WaitingOptions();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 31;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 13;
    }
}
