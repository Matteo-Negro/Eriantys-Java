package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Island: new line movement.
 */
public class CloudNewLine implements DeltaCoordinates {

    private static CloudNewLine instance = null;

    private CloudNewLine() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static CloudNewLine getInstance() {
        if (instance == null)
            instance = new CloudNewLine();
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
        return 1;
    }
}
