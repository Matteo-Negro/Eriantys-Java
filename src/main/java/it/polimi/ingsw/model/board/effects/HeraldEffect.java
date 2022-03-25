package it.polimi.ingsw.model.board.effects;

public class HeraldEffect implements Effect{

    private int cost;

    public HeraldEffect(){
        cost = 3;
    }

    public int getId(){
        return 3;
    }

    public int getCost(){
        return cost;
    }

    public void effect(){

    }

    public void clean(){

    }

}
