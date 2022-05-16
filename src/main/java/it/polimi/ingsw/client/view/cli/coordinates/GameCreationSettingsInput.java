package it.polimi.ingsw.client.view.cli.coordinates;

import it.polimi.ingsw.client.view.cli.DeltaCoordinates;

public class GameCreationSettingsInput implements DeltaCoordinates {
    private static GameCreationSettingsInput instance = null;

    private GameCreationSettingsInput() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static GameCreationSettingsInput getInstance() {
        if (instance == null)
            instance = new GameCreationSettingsInput();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -6;
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
}
