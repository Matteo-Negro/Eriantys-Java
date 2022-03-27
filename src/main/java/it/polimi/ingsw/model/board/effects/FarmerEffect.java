package it.polimi.ingsw.model.board.effects;

public class FarmerEffect implements Effect{

    private Map<HouseColor, Player> stolenProfessors;

    public FarmerEffect(){
        stolenProfessors = new Map<HouseColor, Player>;
    }

    public int getId(){
        return 2;
    }

    public void effect(){

    }

    public void clean(){

    }

    public int setCost(){
        return 2;
    }

    private Map<HouseColor, Player> getStolenProfessors(){
        return stolenProfessors;
    }

    private void stealProfessors(){

    }

    private void returnProfessors(){

    }
}
