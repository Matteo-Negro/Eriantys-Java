package it.polimi.ingsw.client.model;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;

import java.util.HashMap;
import java.util.Map;

public class SchoolBoard {
    private final TowerType towerType;
    private final int towersNumber;
    private final Map<HouseColor, Integer> entrance;
    private final Map<HouseColor, Integer> diningRoom;

    public SchoolBoard(TowerType statusTowerType, int statusTowersNumber, Map<HouseColor, Integer> statusEntrance, Map<HouseColor, Integer> statusDiningRoom) {
        this.towerType = statusTowerType;
        this.towersNumber = statusTowersNumber;
        this.entrance = new HashMap<>(statusEntrance);
        this.diningRoom = new HashMap<>(statusDiningRoom);
    }
}
