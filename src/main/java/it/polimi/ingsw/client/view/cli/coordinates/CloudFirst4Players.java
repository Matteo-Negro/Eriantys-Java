package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Cloud: first position in case of 4 players.
 */
public class CloudFirst4Players implements DeltaCoordinates {

    private static CloudFirst4Players instance = null;

    private CloudFirst4Players() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static CloudFirst4Players getInstance() {
        if (instance == null)
            instance = new CloudFirst4Players();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 22;
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
