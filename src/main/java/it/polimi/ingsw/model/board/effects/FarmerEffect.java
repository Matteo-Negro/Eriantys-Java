package it.polimi.ingsw.model.board.effects;

public class FarmerEffect implements Effect{

    private Map<HouseColor, Player> stolenProfessors;
    private int cost;

    public FarmerEffect(){
        stolenProfessors = new Map<HouseColor, Player>;
        cost = 2;
    }

    public int getId(){
        return 2;
    }

    public int getCost(){
        return cost;
    }

    public void effect(){

    }

    public void clean(){

    }

    private Map<HouseColor, Player> getStolenProfessors(){
        return stolenProfessors;
    }

    private void stealProfessors(){

    }

    private void returnProfessors(){

    }
}
