package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Subtitle: cursor position.
 */
public class Subtitle implements DeltaCoordinates {

    private static Subtitle instance = null;

    private Subtitle() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static Subtitle getInstance() {
        if (instance == null)
            instance = new Subtitle();
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
        return 14;
    }
}
