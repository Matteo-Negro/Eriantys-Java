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

    }

    public void clean(){

    }

    public int setCost(){
        return 2;
    }

    private int getAvailableBans(){
        return availableBans;
    }

    private void takeBan(){

    }

    private void putBan(){

    }
}
