package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

public class GameSettings implements DeltaCoordinates {
    private static GameSettings instance = null;

    private  GameSettings(){
    }

    public static GameSettings getInstance(){
        if (instance == null)
            instance = new GameSettings();
        return instance;
    }

    @Override
    public int getX(){
        return 21;
    }

    @Override
    public int getY() {
        return 4;
    }
}
