package it.polimi.ingsw.model.board.effects;

public class HerablistEffect implements Effect{

    private int availableBans;

    private int cost;

    public HerablistEffect(){
        availableBans = 5;
        cost = 2;
    }

    public int getId(){
        return 5;
    }

    public int getCost(){
        return cost;
    }

    public void effect(){

    }

    public void clean(){

    }

    private int getAvailableBans(){
        return availableBans;
    }

    private void takeBan(){

    }

    private void putBan(){

    }
}
