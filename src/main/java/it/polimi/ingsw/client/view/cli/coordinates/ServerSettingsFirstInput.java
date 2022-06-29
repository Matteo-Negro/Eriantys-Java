package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Server settings: cursor position for first input.
 */
public class ServerSettingsFirstInput implements DeltaCoordinates {

    private static ServerSettingsFirstInput instance = null;

    private ServerSettingsFirstInput() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static ServerSettingsFirstInput getInstance() {
        if (instance == null)
            instance = new ServerSettingsFirstInput();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 32;
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
