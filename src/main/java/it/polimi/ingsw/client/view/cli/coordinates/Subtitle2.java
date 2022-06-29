package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Subtitle: cursor position for the first line.
 */
public class Subtitle2 implements DeltaCoordinates {

    private static Subtitle2 instance = null;

    private Subtitle2() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static Subtitle2 getInstance() {
        if (instance == null)
            instance = new Subtitle2();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -53;
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
