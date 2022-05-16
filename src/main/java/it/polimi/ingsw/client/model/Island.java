package it.polimi.ingsw.client.model;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;

import java.util.EnumMap;
import java.util.Map;

public class Island {
    private final int id;
    private final Island nextIsland;
    private final Island prevIsland;
    private final TowerType tower;
    private final Map<HouseColor, Integer> students;
    private final boolean ban;

    public Island(int statusId, Island statusNextIsland, Island statusPrevIsland, TowerType statusTower, Map<HouseColor, Integer> statusStudents, boolean statusBan) {
        this.id = statusId;
        this.nextIsland = statusNextIsland;
        this.prevIsland = statusPrevIsland;
        this.tower = statusTower;
        this.students = new EnumMap<HouseColor, Integer>(statusStudents);
        this.ban = statusBan;
    }
}
