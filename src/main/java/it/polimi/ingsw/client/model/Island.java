package it.polimi.ingsw.client.model;

import com.google.gson.JsonObject;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.exceptions.IllegalActionException;
import it.polimi.ingsw.utilities.parsers.JsonToObjects;

import java.util.EnumMap;
import java.util.Map;

/**
 * The island on client-side.
 *
 * @author Riccardo Motta
 */
public class Island {

    private final int id;
    private final Map<HouseColor, Integer> students;
    private final boolean motherNature;
    private boolean ban;
    private boolean next;
    private boolean prev;
    private TowerType tower;

    /**
     * Default constructor.
     *
     * @param id           The id of the island.
     * @param next         true if connected to next island.
     * @param prev         true if connected to previous island.
     * @param tower        The tower on the island.
     * @param students     The students on the island.
     * @param motherNature true if Mother Nature is on the island.
     * @param ban          true if the island is banned.
     */
    public Island(int id, boolean next, boolean prev, TowerType tower, JsonObject students, boolean motherNature, boolean ban) {
        this.id = id;
        this.next = next;
        this.prev = prev;
        this.tower = tower;
        this.motherNature = motherNature;
        this.ban = ban;
        if (students != null)
            this.students = JsonToObjects.parseStudents(students);
        else {
            this.students = new EnumMap<>(HouseColor.class);
            for (HouseColor color : HouseColor.values()) this.students.put(color, 0);
        }
    }

    /**
     * Returns the island id.
     *
     * @return The id attribute.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the students on the island.
     *
     * @return The students on the island.
     */
    public Map<HouseColor, Integer> getStudents() {
        return new EnumMap<>(students);
    }

    /**
     * Adds a student to the island.
     *
     * @param color The color of the student.
     */
    public void addStudent(HouseColor color) {
        students.replace(color, students.get(color) + 1);
    }

    /**
     * Removes a student from the island.
     *
     * @param color The color of the student.
     * @throws IllegalActionException Thrown if the student is not present.
     */
    public void removeStudent(HouseColor color) throws IllegalActionException {
        if (students.get(color) == 0)
            throw new IllegalActionException();
        students.replace(color, students.get(color) - 1);
    }

    /**
     * Gets the tower.
     *
     * @return The tower.
     */
    public TowerType getTower() {
        return tower;
    }

    /**
     * Sets the tower on the island.
     *
     * @param tower The tower to put on the island.
     */
    public void setTower(TowerType tower) {
        this.tower = tower;
    }

    /**
     * Gets if Mother Nature is on the island.
     *
     * @return true if Mother Nature is on the island.
     */
    public boolean hasMotherNature() {
        return motherNature;
    }

    /**
     * Tells if the island is banned.
     *
     * @return true if the island is banned.
     */
    public boolean isBanned() {
        return ban;
    }

    /**
     * Bans the island.
     */
    public void setBan() {
        ban = true;
    }

    /**
     * Unbans the island.
     */
    public void removeBan() {
        ban = false;
    }

    /**
     * Tells if the island is connected to the next one.
     *
     * @return true if connected.
     */
    public boolean hasNext() {
        return next;
    }

    /**
     * Connects the island to the next one.
     */
    public void setNext() {
        next = true;
    }

    /**
     * Tells if the island is connected to the previous one.
     *
     * @return true if connected.
     */
    public boolean hasPrev() {
        return prev;
    }

    /**
     * Connects the island to the previous one.
     */
    public void setPrev() {
        prev = true;
    }
}
