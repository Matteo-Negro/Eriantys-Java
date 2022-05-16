package it.polimi.ingsw.client.view.cli.coordinates;

import it.polimi.ingsw.client.view.cli.DeltaCoordinates;

public class GameSettingsNewLine implements DeltaCoordinates {
    private static GameSettingsNewLine instance = null;

    private GameSettingsNewLine() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static GameSettingsNewLine getInstance() {
        if (instance == null)
            instance = new GameSettingsNewLine();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -76;
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
