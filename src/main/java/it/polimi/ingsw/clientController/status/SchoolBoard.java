package it.polimi.ingsw.clientController.status;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;

import java.util.HashMap;
import java.util.Map;

public class SchoolBoard {
    private TowerType towerType;
    private int towersNumber;
    private Map<HouseColor, Integer> entrance;
    private Map<HouseColor, Integer> diningRoom;

    public SchoolBoard(TowerType statusTowerType, int statusTowersNumber, Map<HouseColor, Integer> statusEntrance, Map<HouseColor, Integer> statusDiningRoom){
        this.towerType = statusTowerType;
        this.towersNumber = statusTowersNumber;
        this.entrance = new HashMap<>(statusEntrance);
        this.diningRoom = new HashMap<>(statusDiningRoom);
    }
}
