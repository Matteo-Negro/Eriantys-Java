package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

/**
 * Server settings: cursor position for first input.
 */
public class ServerSettingsFirstInput implements DeltaCoordinates {

    private static ServerSettingsFirstInput instance = null;

    private ServerSettingsFirstInput() {
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
        return -2;
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
}
