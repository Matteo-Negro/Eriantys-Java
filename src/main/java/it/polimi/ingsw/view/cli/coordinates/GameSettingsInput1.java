package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

public class GameSettingsInput1 implements DeltaCoordinates {
    private static GameSettingsInput1 instance = null;

    private GameSettingsInput1(){
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static GameSettingsInput1 getInstance(){
        if (instance == null)
            instance = new GameSettingsInput1();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX(){
        return -15;
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
