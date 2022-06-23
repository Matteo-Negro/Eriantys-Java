package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.TowerType;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Island Class, board main element.
 *
 * @author Matteo Negro
 */

public class Island {
    private final int id;
    private final Map<HouseColor, Integer> students;
    private int size;
    private boolean ban;
    private TowerType tower;

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
        this.students = new EnumMap<>(HouseColor.class);
        if (color == null)
            Arrays.stream(HouseColor.values()).forEach(studentColor -> this.students.put(studentColor, 0));
        else
            Arrays.stream(HouseColor.values()).forEach(studentColor -> this.students.put(studentColor, studentColor.equals(color) ? 1 : 0));
        Log.info("*** New Island successfully created with id: " + idIsland);
    }

    /**
     * Island Constructor, all the attributes are initialized to the saved state.
     *
     * @param status   A map that contains the number of students for each color.
     * @param idIsland The id of the island that is initialized.
     * @param size     The size of the island that is initialized.
     * @param ban      The status of ban on the island that is initialized.
     * @param tower    The tower of the player that rules the island.
     */
    public Island(Map<HouseColor, Integer> status, int idIsland, int size, boolean ban, TowerType tower) {
        this.id = idIsland;
        this.size = size;
        this.ban = ban;
        this.tower = tower;
        this.students = new EnumMap<>(HouseColor.class);

        for (HouseColor color : HouseColor.values())
            this.students.put(color, status.getOrDefault(color, 0));

        Log.info("*** Saved Island successfully restored with id: " + idIsland);
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
     * This method returns the island's size, default second is 1, but due to group aggregations size could rise.
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
     * @return An HashMap (first: HouseColor, second: Integer), that contains the number of students on the island.
     */
    public Map<HouseColor, Integer> getStudents() {
        return new EnumMap<>(this.students);
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
     * @return The boolean second of ban.
     */
    public boolean isBanned() {
        return this.ban;
    }

    /**
     * This method set true the second of ban.
     */
    public void setBan() {
        this.ban = true;
    }

    /**
     * This method set false the second of ban.
     */
    public void removeBan() {
        this.ban = false;
    }

    /**
     * Standard redefinition of "equals" method.
     *
     * @param o Object to compare.
     * @return true if the two objects are the same.
     */
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || o.getClass() != this.getClass()) return false;

        Island that = (Island) o;
        return id == that.getId() && this.students.equals(that.students) && this.size == that.size && this.ban == that.ban && this.tower.equals(that.tower);
    }

    /**
     * Calculates the hash.
     *
     * @return The calculated hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, students, size, ban, tower);
    }
}
