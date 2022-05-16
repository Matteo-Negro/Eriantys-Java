package it.polimi.ingsw.client.controller.status;

public class Assistant {
    private final int id;
    private final int maxDistance;
    private final boolean bonus;

    public Assistant(int statusId, int statusMaxDistance, boolean statusBonus) {
        this.id = statusId;
        this.maxDistance = statusMaxDistance;
        this.bonus = statusBonus;
    }
}
