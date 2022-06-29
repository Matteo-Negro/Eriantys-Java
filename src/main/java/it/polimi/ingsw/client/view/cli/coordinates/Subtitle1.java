package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Subtitle: cursor position for the first line.
 */
public class Subtitle1 implements DeltaCoordinates {

    private static Subtitle1 instance = null;

    private Subtitle1() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static Subtitle1 getInstance() {
        if (instance == null)
            instance = new Subtitle1();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 14;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 0;
    }
}
