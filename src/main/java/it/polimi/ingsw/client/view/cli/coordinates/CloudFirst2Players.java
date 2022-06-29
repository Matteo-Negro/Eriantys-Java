package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Cloud: first position in case of 2 players.
 */
public class CloudFirst2Players implements DeltaCoordinates {

    private static CloudFirst2Players instance = null;

    private CloudFirst2Players() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static CloudFirst2Players getInstance() {
        if (instance == null)
            instance = new CloudFirst2Players();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 42;
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
