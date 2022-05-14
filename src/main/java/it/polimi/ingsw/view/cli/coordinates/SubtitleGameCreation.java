package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

public class SubtitleGameCreation implements DeltaCoordinates {
    private static SubtitleGameCreation instance = null;

    private  SubtitleGameCreation(){
    }

    public static SubtitleGameCreation getInstance(){
        if (instance == null)
            instance = new SubtitleGameCreation();
        return instance;
    }

    @Override
    public int getX(){
        return 26;
    }

    @Override
    public int getY() {
        return 0;
    }
}
