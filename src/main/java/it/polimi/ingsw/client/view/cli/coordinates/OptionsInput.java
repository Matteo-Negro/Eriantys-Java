package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Options: cursor position for input.
 */
public class OptionsInput implements DeltaCoordinates {

    private static OptionsInput instance = null;

    private OptionsInput() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static OptionsInput getInstance() {
        if (instance == null)
            instance = new OptionsInput();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 18;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 4;
    }
}
