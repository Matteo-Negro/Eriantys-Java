package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Win page: new line movement.
 */
public class WinNewLine implements DeltaCoordinates {

    private static WinNewLine instance = null;

    private WinNewLine() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static WinNewLine getInstance() {
        if (instance == null)
            instance = new WinNewLine();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -39;
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
