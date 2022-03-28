package it.polimi.ingsw.model.board.effects;

public class HerablistEffect implements Effect{

    private int availableBans;

    public HerablistEffect(){
        availableBans = 5;
    }

    public int getId(){
        return 5;
    }

    public void effect(){
        takeBan();
    }

    public void clean(){

    }

    public int getCost(){
        return 2;
    }

    private int getAvailableBans(){
        return availableBans;
    }

    private void takeBan(){
        availableBans--;
    }

    private void putBan(){
        availableBans++;
    }
}
