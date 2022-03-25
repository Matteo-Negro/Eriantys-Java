package it.polimi.ingsw.model.board.effects;

public class MinstrelEffect implements Effect{

    private int cost;

    public MinstrelEffect(){
        cost = 1;
    }

    public int getId(){
        return 10;
    }

    public int getCost(){
        return cost;
    }

    public void effect(){

    }

    public void clean(){

    }
}
