package it.polimi.ingsw.model.board.effects;

import java.util.ArrayList;

public class CentaurEffect implements Effect{

    private List<Island> takenTowers;
    private int cost;

    public CentaurEffect(){
        takenTowers = new ArrayList<Island>();
        cost = 3;
    }

    public int getId(){
        return 6;
    }

    public int getCost(){
        return cost;
    }

    public void effect(){

    }

    public void clean(){

    }

    private List<Island> getTakenTowers(){
        return takenTowers;
    }

    private void takeTowers(){

    }

    private void returnTowers(){

    }
}
