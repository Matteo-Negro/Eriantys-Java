package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Login: new line movement.
 */
public class LoginNewLine implements DeltaCoordinates {

    private static LoginNewLine instance = null;

    private LoginNewLine() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static LoginNewLine getInstance() {
        if (instance == null)
            instance = new LoginNewLine();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -35;
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
