package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Waiting room: new line movement.
 */
public class WaitingNewLine implements DeltaCoordinates {

    private static WaitingNewLine instance = null;

    private WaitingNewLine() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static WaitingNewLine getInstance() {
        if (instance == null)
            instance = new WaitingNewLine();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -37;
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
