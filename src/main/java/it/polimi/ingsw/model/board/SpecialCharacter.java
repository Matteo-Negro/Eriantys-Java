package it.polimi.ingsw.model.board.effects;

public class CharacterCard {

    private final int id;
    private int effectCost;
    private boolean alreadyPayed;
    private boolean payedInRound;
    private boolean isActive;
    private final Effect assignedEffect;


    public CharacterCard(int id, Effect assignedEffect){
        this.id = id;
        this.assignedEffect = assignedEffect;
        effectCost = assignedEffect.getCost();
        isActive = false;
        alreadyPayed = false;
        payedInRound = false;
    }

    public int getId(){
        return id;
    }

    public int getEffectCost(){

        if(alreadyPayed)  return effectCost+1;

        else return effectCost;

    }

    public Effect getEffect(){
        return assignedEffect;
    }

    public void payCost(){
        alreadyPayed = true;
        payedInRound = true;
        isActive = true;
        getEffect().effect();
    }

    public void cleanEffect(){
        isActive = false;
        assignedEffect.clean();
    }

    public void changedRound(){
        payedInRound = false;
    }
}
