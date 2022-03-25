package it.polimi.ingsw.model.board.effects;

public class ThiefEffect implements Effect{

    private int cost;

    public ThiefEffect(){
        cost = 3;
    }

    public int getId(){
        return 12;
    }

    public int getCost(){
        return cost;
    }

    public void effect(){

    }

    public void clean() {

    }
}
