package it.polimi.ingsw.model.board.effects;

public class MushroomerEffect implements Effect{

    private int cost;

    public MushroomerEffect(){
        cost = 3;
    }

    public int getId(){
        return 9;
    }

    public int getCost(){
        return cost;
    }

    public void effect(){

    }

    public void clean(){

    }
}
