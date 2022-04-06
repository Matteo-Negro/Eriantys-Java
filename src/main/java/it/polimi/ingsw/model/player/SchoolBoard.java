package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.utilities.HouseColor;
import it.polimi.ingsw.model.utilities.TowerType;
import it.polimi.ingsw.model.utilities.exceptions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the board on which each player has their own towers, students and professors.
 *
 * @author Riccardo Motta
 */
public class SchoolBoard {

    private final TowerType towerType;
    private final Map<HouseColor, Integer> diningRoom;
    private final Map<HouseColor, Integer> entrance;
    private final Map<HouseColor, Boolean> professors;
    private int towersNumber;

    /**
     * Class constructor.
     *
     * @param towersNumber Number of towers to put on the board.
     * @param towerType    Color of the tower.
     */
    SchoolBoard(int towersNumber, TowerType towerType) {
        this.towersNumber = towersNumber;
        this.towerType = towerType;
        diningRoom = new HashMap<>();
        entrance = new HashMap<>();
        professors = new HashMap<>();
        Arrays.stream(HouseColor.values())
                .forEach(color -> {
                    diningRoom.put(color, 0);
                    entrance.put(color, 0);
                    professors.put(color, false);
                });
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
        return new HashMap<>(entrance);
    }

    /**
     * Gets a map of the students in the dining room.
     *
     * @return Map of the students in the dining room.
     */
    public Map<HouseColor, Integer> getDiningRoom() {
        return new HashMap<>(diningRoom);
    }

    /**
     * Gets a map of the professors.
     *
     * @return Map of the professors.
     */
    public Map<HouseColor, Boolean> getProfessors() {
        return new HashMap<>(professors);
    }

    /**
     * Gets the number of students of the specified color.
     *
     * @param houseColor Color of the students.
     * @return Number of students of the specified color.
     */
    public int getStudentsNumberOf(HouseColor houseColor) {
        return diningRoom.get(houseColor);
    }

    /**
     * Gets the required professor is present.
     *
     * @param houseColor Color of the professor.
     * @return If the required professor is present.
     */
    public boolean hasProfessorOf(HouseColor houseColor) {
        return professors.get(houseColor);
    }

    /**
     * Adds the specified professor.
     *
     * @param professor Color of the professor to add.
     */
    public void addProfessor(HouseColor professor) throws AlreadyPresentProfessorException {
        if (professors.get(professor))
            throw new AlreadyPresentProfessorException("The required professor (" + professor + ") is already present.");
        professors.replace(professor, true);
    }

    /**
     * Removes the specified professor.
     *
     * @param professor Color of the professor to remove.
     * @throws NoProfessorException If the professor wasn't there.
     */
    public void removeProfessor(HouseColor professor) throws NoProfessorException {
        if (!professors.get(professor))
            throw new NoProfessorException("The required professor (" + professor + ") is not present.");
        professors.replace(professor, false);
    }

    /**
     * Adds the students to the entrance
     *
     * @param students Students to add.
     */
    public void addToEntrance(Map<HouseColor, Integer> students) {
        Arrays.stream(HouseColor.values())
                .forEach(color -> entrance.replace(color, entrance.get(color) + students.get(color)));
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
        if (number < 0)
            throw new NegativeException("Given value is negative (" + number + ")");
        if (towersNumber < number)
            throw new NotEnoughTowersException("Required towers (" + number + ") is more than available (" + towersNumber + ")");
        towersNumber -= number;
    }

    /**
     * Increases the number of towers by a specified number.
     *
     * @param number Number of towers to add.
     * @throws NegativeException If the number of towers is negative.
     */
    public void addTowers(int number) throws NegativeException {
        if (number < 0)
            throw new NegativeException("Given value is negative (" + number + ")");
        towersNumber += number;
    }
}
