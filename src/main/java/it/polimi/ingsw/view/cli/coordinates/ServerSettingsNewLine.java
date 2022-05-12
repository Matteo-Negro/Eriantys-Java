package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

/**
 * Server settings: new line movement.
 */
public class ServerSettingsNewLine implements DeltaCoordinates {

    private static ServerSettingsNewLine instance = null;

    private ServerSettingsNewLine() {
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -67;
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

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static ServerSettingsNewLine getInstance() {
        if (instance == null)
            instance = new ServerSettingsNewLine();
        return instance;
    }
}
