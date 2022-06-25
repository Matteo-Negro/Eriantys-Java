package it.polimi.ingsw.client.model;

import com.google.gson.JsonObject;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.exceptions.NegativeException;
import it.polimi.ingsw.utilities.exceptions.NoStudentException;
import it.polimi.ingsw.utilities.exceptions.NotEnoughTowersException;
import it.polimi.ingsw.utilities.parsers.JsonToObjects;

import java.util.EnumMap;
import java.util.Map;

/**
 * This class represents the board on which each player has their own towers, students and professors on the client side.
 *
 * @author Matteo Negro
 */
public class SchoolBoard {
    private final TowerType towerType;
    private final Map<HouseColor, Integer> entrance;
    private final Map<HouseColor, Integer> diningRoom;
    private final Map<HouseColor, Boolean> professors;
    private int towersNumber;

    /**
     * Class constructor.
     *
     * @param towersNumber Number of towers to put on the board.
     * @param towerType    Color of the tower.
     * @param diningRoom   Students in the dining room.
     * @param entrance     Students at the entrance.
     */
    public SchoolBoard(TowerType towerType, int towersNumber, JsonObject entrance, JsonObject diningRoom) {
        this.towerType = towerType;
        this.towersNumber = towersNumber;
        this.entrance = JsonToObjects.parseStudents(entrance);
        this.diningRoom = JsonToObjects.parseStudents(diningRoom);
        this.professors = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values())
            professors.put(color, false);
    }

    /**
     * Gets the type of the tower linked to the board.
     *
     * @return Type of the tower linked to the board.
     */
    public TowerType getTowerType() {
        return this.towerType;
    }

    /**
     * Gets the number of towers already on the board.
     *
     * @return Number of towers already on the board.
     */
    public int getTowersNumber() {
        return this.towersNumber;
    }

    /**
     * Increases the number of towers by a specified number.
     *
     * @param number Number of towers to add.
     * @throws NegativeException If the number of towers is negative.
     */
    public void addTowers(int number) throws NegativeException {
        if (number < 0) throw new NegativeException("Given second is negative (" + number + ")");
        this.towersNumber += number;
    }

    /**
     * Removes the number of towers by a specified number.
     *
     * @param number Number of towers to remove.
     * @throws NegativeException        If the number of towers is negative.
     * @throws NotEnoughTowersException If the number of available towers is less than required.
     */
    public void removeTowers(int number) throws NegativeException, NotEnoughTowersException {
        if (number < 0) throw new NegativeException("Given second is negative (" + number + ")");
        if (this.towersNumber < number) {
            this.towersNumber = 0;
            throw new NotEnoughTowersException("Required towers (" + number + ") is more than available (" + this.towersNumber + ")");
        }
        this.towersNumber -= number;
    }

    /**
     * Gets a map of the students at the entrance.
     *
     * @return Map of the students at the entrance.
     */
    public Map<HouseColor, Integer> getEntrance() {
        return new EnumMap<>(this.entrance);
    }

    /**
     * Adds the students to the entrance.
     *
     * @param students Students to add.
     */
    public void addToEntrance(Map<HouseColor, Integer> students) {
        for (HouseColor color : HouseColor.values())
            this.entrance.replace(color, this.entrance.get(color) + students.get(color));
    }

    /**
     * Removes the required student from the entrance.
     *
     * @param student The student to remove.
     * @throws NoStudentException If there is no student of that color.
     */
    public void removeFromEntrance(HouseColor student) throws NoStudentException {
        if (this.entrance.get(student) == 0)
            throw new NoStudentException("The required student (" + student + ") is not present.");
        this.entrance.replace(student, this.entrance.get(student) - 1);
    }

    /**
     * Gets a map of the students in the dining room.
     *
     * @return Map of the students in the dining room.
     */
    public Map<HouseColor, Integer> getDiningRoom() {
        return new EnumMap<>(this.diningRoom);
    }

    /**
     * Adds a student to the dining room.
     *
     * @param student The student to be added.
     */
    public void addToDiningRoom(HouseColor student) {
        this.diningRoom.replace(student, this.diningRoom.get(student) + 1);
    }

    /**
     * Removes a student to the dining room.
     *
     * @param student The student to be removed.
     * @throws NoStudentException If there is no student of that color.
     */
    public void removeFromDiningRoom(HouseColor student) throws NoStudentException {
        if (this.diningRoom.get(student) == 0)
            throw new NoStudentException("The required student (" + student + ") is not present.");
        this.diningRoom.replace(student, this.diningRoom.get(student) - 1);
    }

    /**
     * Gets a map of the professors.
     *
     * @return The map of the professors.
     */
    public Map<HouseColor, Boolean> getProfessors() {
        return new EnumMap<>(this.professors);
    }

    /**
     * Adds a student to the dining room.
     *
     * @param professor The color of the professor.
     */
    public void addProfessor(HouseColor professor) {
        this.professors.put(professor, true);
    }

    /**
     * Removes a student to the dining room.
     *
     * @param professor The color of the professor.
     */
    public void removeProfessor(HouseColor professor) {
        this.professors.put(professor, false);
    }
}
