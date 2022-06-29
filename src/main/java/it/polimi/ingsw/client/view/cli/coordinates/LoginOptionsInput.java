package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Login options: cursor position for input.
 */
public class LoginOptionsInput implements DeltaCoordinates {

    private static LoginOptionsInput instance = null;

    private LoginOptionsInput() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static LoginOptionsInput getInstance() {
        if (instance == null)
            instance = new LoginOptionsInput();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -20;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return -1;
    }
}
