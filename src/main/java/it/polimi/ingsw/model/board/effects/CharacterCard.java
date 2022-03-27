package it.polimi.ingsw.model.board.effects;

public class CharacterCard {

    private int id;
    private int cost;
    private boolean alreadyPayed;
    private boolean payedInRound;
    private Effect assignedEffect;

    public CharacterCard(int id, Effect assignedEffect){
        this.id = id;
        this.assignedEffect = assignedEffect;
        cost = assignedEffect.setCost();
    }

    public int getId(){
        return id;
    }

    public int getCost(){
        if(alreadyPayed)
            return cost+1;

        else
            return cost;
    }

    public Effect getEffect(){
        return assignedEffect;
    }

    public void payCost(){
        alreadyPayed = true;
        getEffect().effect();
    }

}
