package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

public class GameCreationSettings implements DeltaCoordinates {
    private static GameCreationSettings instance = null;

    private GameCreationSettings(){
    }

    public static GameCreationSettings getInstance(){
        if (instance == null)
            instance = new GameCreationSettings();
        return instance;
    }

    @Override
    public int getX(){
        return 47;
    }

    @Override
    public int getY() {
        return 4;
    }
}
