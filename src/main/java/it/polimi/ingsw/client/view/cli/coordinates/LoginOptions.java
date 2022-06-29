package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Login options: cursor position.
 */
public class LoginOptions implements DeltaCoordinates {

    private static LoginOptions instance = null;

    private LoginOptions() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static LoginOptions getInstance() {
        if (instance == null)
            instance = new LoginOptions();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 3;
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
