package it.polimi.ingsw.model.board.effects;

public class KnightEffect implements Effect{

    private int cost;

    public KnightEffect(){
        cost = 2;
    }

    public int getId(){
        return 8;
    }

    public int getCost(){
        return cost;
    }

    public void effect(){

    }

    public void clean(){

    }
}
