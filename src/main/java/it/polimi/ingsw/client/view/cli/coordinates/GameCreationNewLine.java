package it.polimi.ingsw.client.view.cli.coordinates;

import it.polimi.ingsw.client.view.cli.DeltaCoordinates;

public class GameCreationNewLine implements DeltaCoordinates {
    private static GameCreationNewLine instance = null;

    private GameCreationNewLine() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static GameCreationNewLine getInstance() {
        if (instance == null)
            instance = new GameCreationNewLine();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -23;
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
