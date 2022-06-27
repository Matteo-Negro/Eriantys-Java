package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.exceptions.NegativeException;
import it.polimi.ingsw.utilities.exceptions.NoStudentException;
import it.polimi.ingsw.utilities.exceptions.NotEnoughTowersException;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class represents the board on which each player has their own towers, students and professors.
 *
 * @author Riccardo Motta
 */
public class SchoolBoard {

    private final TowerType towerType;
    private final Map<HouseColor, Integer> entrance;
    private final Map<HouseColor, Integer> diningRoom;
    private int towersNumber;

    /**
     * Class constructor.
     *
     * @param towersNumber Number of towers to put on the board.
     * @param towerType    Color of the tower.
     */
    SchoolBoard(int towersNumber, TowerType towerType, Map<HouseColor, Integer> students) {
        this.towersNumber = towersNumber;
        this.towerType = towerType;
        this.diningRoom = new EnumMap<>(HouseColor.class);
        this.entrance = new EnumMap<>(students);
        for (HouseColor color : HouseColor.values()) {
            this.diningRoom.put(color, 0);
        }
        Log.info("*** New SchoolBoard successfully created.");
    }

    /**
     * Class constructor used to restore the game.
     *
     * @param towersNumber Number of towers to put on the board.
     * @param towerType    Color of the tower.
     * @param diningRoom   Students in the dining room.
     * @param entrance     Students at the entrance.
     */
    public SchoolBoard(int towersNumber, TowerType towerType, Map<HouseColor, Integer> diningRoom, Map<HouseColor, Integer> entrance) {
        this.towersNumber = towersNumber;
        this.towerType = towerType;
        this.diningRoom = new EnumMap<>(diningRoom);
        this.entrance = new EnumMap<>(entrance);

        Log.info("*** Saved SchoolBoard successfully restored.");
    }

    /**
     * Gets the number of towers already on the board.
     *
     * @return Number of towers already on the board.
     */
    public int getTowersNumber() {
        return towersNumber;
    }

    /**
     * Gets the type of the tower linked to the board.
     *
     * @return Type of the tower linked to the board.
     */
    public TowerType getTowerType() {
        return towerType;
    }

    /**
     * Gets a map of the students at the entrance.
     *
     * @return Map of the students at the entrance.
     */
    public Map<HouseColor, Integer> getEntrance() {
        return new EnumMap<>(entrance);
    }

    /**
     * Gets a map of the students in the dining room.
     *
     * @return Map of the students in the dining room.
     */
    public Map<HouseColor, Integer> getDiningRoom() {
        return new EnumMap<>(diningRoom);
    }

    /**
     * Gets the number of students of the specified color into the dining room.
     *
     * @param houseColor Color of the students.
     * @return Number of students of the specified color.
     */
    public int getStudentsNumberOf(HouseColor houseColor) {
        return diningRoom.get(houseColor);
    }

    /**
     * Adds the students to the entrance
     *
     * @param students Students to add.
     */
    public void addToEntrance(Map<HouseColor, Integer> students) {
        for (HouseColor color : students.keySet())
            entrance.replace(color, entrance.get(color) + students.get(color));
    }

    /**
     * Removes the required student from the entrance.
     *
     * @param student The student to remove.
     * @throws NoStudentException If there is no student of that color.
     */
    public void removeFromEntrance(HouseColor student) throws NoStudentException {
        if (entrance.get(student) == 0)
            throw new NoStudentException("The required student (" + student + ") is not present.");
        entrance.replace(student, entrance.get(student) - 1);
    }

    /**
     * Adds a student to the dining room.
     *
     * @param student The student to be added.
     */
    public void addToDiningRoom(HouseColor student) {
        diningRoom.replace(student, diningRoom.get(student) + 1);
    }

    /**
     * Removes a student to the dining room.
     *
     * @param student The student to be removed.
     * @throws NoStudentException If there is no student of that color.
     */
    public void removeFromDiningRoom(HouseColor student) throws NoStudentException {
        if (diningRoom.get(student) == 0)
            throw new NoStudentException("The required student (" + student + ") is not present.");
        diningRoom.replace(student, diningRoom.get(student) - 1);
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
        if (towersNumber < number) {
            towersNumber = 0;
            throw new NotEnoughTowersException("Required towers (" + number + ") is more than available (" + towersNumber + ")");
        }
        towersNumber -= number;
    }

    /**
     * Increases the number of towers by a specified number.
     *
     * @param number Number of towers to add.
     * @throws NegativeException If the number of towers is negative.
     */
    public void addTowers(int number) throws NegativeException {
        if (number < 0) throw new NegativeException("Given second is negative (" + number + ")");
        towersNumber += number;
    }

    /**
     * Standard redefinition of "equals" method.
     *
     * @param o Object to compare.
     * @return true if the two objects are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchoolBoard that = (SchoolBoard) o;
        return towersNumber == that.towersNumber && towerType == that.towerType && Objects.equals(entrance, that.entrance) && Objects.equals(diningRoom, that.diningRoom);
    }

    /**
     * Calculates the hash.
     *
     * @return The calculated hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(towerType, entrance, diningRoom, towersNumber);
    }
}
