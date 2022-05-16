package it.polimi.ingsw.clientController.status;

public class Assistant {
    private int id;
    private int maxDistance;
    private boolean bonus;

    public Assistant(int statusId, int statusMaxDistance, boolean statusBonus){
        this.id = statusId;
        this.maxDistance = statusMaxDistance;
        this.bonus = statusBonus;
    }
}
