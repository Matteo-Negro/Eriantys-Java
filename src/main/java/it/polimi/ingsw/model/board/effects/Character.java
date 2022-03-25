package it.polimi.ingsw.model.board.effects;

public class Character {

    private int id;
    private boolean alreadyPayed;
    private boolean payedInRound;
    private Effect assignedEffect;

    public Character(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public int getCost(){
        if(alreadyPayed)
            return getEffect().getCost()+1;

        else
            return getEffect().getCost();
    }

    public Effect getEffect(){
        return assignedEffect;
    }

    public void payCost(){
        alreadyPayed = true;
        getEffect().effect();
    }

}
