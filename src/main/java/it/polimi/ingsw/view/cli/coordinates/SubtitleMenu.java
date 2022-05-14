package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

public class SubtitleMenu implements DeltaCoordinates {

    private static SubtitleMenu instance = null;

    private  SubtitleMenu(){
    }

    public static SubtitleMenu getInstance(){
        if (instance == null)
            instance = new SubtitleMenu();
        return instance;
    }

    @Override
    public int getX(){
        return 28;
    }

    @Override
    public int getY() {
        return 0;
    }
}
