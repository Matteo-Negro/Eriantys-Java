package it.polimi.ingsw.model.board;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Island Class, board main element.
 *
 * @author Matteo Negro
 */

public class Island {
    private final int id;
    private int size;
    private boolean ban;
    private TowerType tower;
    private Map<HouseColor, Integer> students;

    /**
     * Island Constructor, all the attributes are initialized.
     *
     * @param color    The first student that is placed on the island when is built, could be null.
     * @param idIsland The island's id.
     */
    public Island(HouseColor color, int idIsland) {
        this.id = idIsland;
        this.size = 1;
        this.ban = false;
        this.tower = null;
        Arrays.stream(HouseColor.values()).forEach(studentColor -> this.students.put(studentColor, studentColor.equals(color) ? 1 : 0));
    }

    /**
     * This method returns the island's id.
     *
     * @return Island's id.
     */
    public int getId() {
        return this.id;
    }

    /**
     * This method returns the island's size, default value is 1, but due to group aggregations size could rise.
     *
     * @return The island's size.
     */
    public int getSize() {
        return this.size;
    }

    /**
     * This method gives access to the island's size.
     *
     * @param newSize The new size of the island.
     */
    public void setSize(int newSize) {
        this.size = newSize;
    }

    /**
     * This method returns the tower's color that occupied the island.
     *
     * @return The tower's color.
     */
    public TowerType getTower() {
        return this.tower;
    }

    /**
     * This method gives access to the tower's color.
     *
     * @param tower The new color of the tower on the island.
     */
    public void setTower(TowerType tower) {
        this.tower = tower;
    }

    /**
     * This method returns a copy of the data structure, that contains the students on the island.
     *
     * @return An HashMap (key: HouseColor, value: Integer), that contains the number of students on the island.
     */
    public Map<HouseColor, Integer> getStudents() {
        return new HashMap<>(this.students);
    }

    /**
     * This method gives access to data structure studentsColor to add students.
     *
     * @param studentColor The color of the student that the player would like to add.
     */
    public void addStudent(HouseColor studentColor) {
        this.students.put(studentColor, this.students.get(studentColor) + 1);
    }

    /**
     * This method gives the status of the attribute 'ban'.
     *
     * @return The boolean value of ban.
     */
    public boolean isBanned() {
        return this.ban;
    }

    /**
     * This method set true the value of ban.
     */
    public void setBan() {
        this.ban = true;
    }

    /**
     * This method set false the value of ban.
     */
    public void removeBan() {
        this.ban = false;
    }
}
