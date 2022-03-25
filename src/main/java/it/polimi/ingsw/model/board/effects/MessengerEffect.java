package it.polimi.ingsw.model.board.effects;

public class MessengerEffect implements Effect{

    private int cost;

    public MessengerEffect(){
        cost = 1;
    }

    public int getId(){
        return 4;
    }

    public int getCost(){
        return cost;
    }

    public void effect(){

    }
    public void clean(){

    }

}
