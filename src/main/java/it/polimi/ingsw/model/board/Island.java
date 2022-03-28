package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.utilities.HouseColor;
import it.polimi.ingsw.model.utilities.TowerType;

import java.util.Map;

/**
 * Island Class, board main element
 *
 * @author Matteo Negro
 */

public class Island {
    private int id;
    private int size;
    private boolean ban;
    private TowerType tower;
    private Map<HouseColor, Integer> students;

    public Island(HouseColor color, int idIsland) {
    }

    public int getId() {
        return 0;
    }

    public TowerType getTower() {
        return null;
    }

    public int getSize() {
        return 0;
    }

    public boolean isBanned() {
        return false;
    }

    public Map<HouseColor, Integer> getStudents() {
        return null;
    }

    public Map<Player, Integer> getInfluence() {
        return null;
    }

    public Map<Player, Integer> getInfluence(HouseColor banColor) {
        return null;
    }

    private int getInfluence(Player player, HouseColor targetColor) {
        return 0;
    }

    public void addStudent(HouseColor studentColor) {
    }

    public void setTower(TowerType tower) {
    }

    public void setBan() {
    }

    public void removeBan() {
    }
}
