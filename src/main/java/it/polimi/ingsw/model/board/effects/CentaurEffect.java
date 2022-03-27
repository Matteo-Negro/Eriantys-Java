package it.polimi.ingsw.model.board.effects;

import java.util.ArrayList;

public class CentaurEffect implements Effect{

    private List<Island> takenTowers;

    public CentaurEffect(){
        takenTowers = new ArrayList<Island>();
    }

    public int getId(){
        return 6;
    }

    public void effect(){

    }

    public void clean(){

    }

    public int setCost(){
        return 3;
    }

    private List<Island> getTakenTowers(){
        return takenTowers;
    }

    private void takeTowers(){

    }

    private void returnTowers(){

    }
}
