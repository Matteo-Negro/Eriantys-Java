package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.exceptions.NegativeException;
import it.polimi.ingsw.utilities.exceptions.NoStudentException;
import it.polimi.ingsw.utilities.exceptions.NotEnoughTowersException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test of class SchoolBoard.
 *
 * @author Matteo Negro
 */
class SchoolBoardTest {

    private final int towersNumber = 8;
    private final TowerType towerType = TowerType.BLACK;
    private final Map<HouseColor, Integer> entrance = new EnumMap<>(HouseColor.class);

    private SchoolBoard schoolBoard;

    /**
     * Generates a new SchoolBoard.
     */
    @BeforeEach
    void setUp() {
        for(HouseColor color : HouseColor.values()) entrance.put(color, 0);
        this.schoolBoard = new SchoolBoard(this.towersNumber, this.towerType, this.entrance);
    }

    /**
     * Allows the garbage collector to delete the created Assistant.
     */
    @AfterEach
    void tearDown() {
        this.schoolBoard = null;
    }

    /**
     * Tests whether returns the number of towers.
     */
    @Test
    void getTowersNumber() {
        assertEquals(this.towersNumber, this.schoolBoard.getTowersNumber());
    }

    /**
     * Tests whether returns the color of the tower.
     */
    @Test
    void getTowerType() {
        assertEquals(this.towerType, this.schoolBoard.getTowerType());
    }

    /**
     * Tests whether returns the list of students in the entrance.
     */
    @Test
    void getEntrance() {
        Map<HouseColor, Integer> tmp = this.schoolBoard.getEntrance();

        for (HouseColor color : HouseColor.values()) assertEquals(0, tmp.get(color));
    }

    /**
     * Tests whether returns the list of students in the dining room.
     */
    @Test
    void getDiningRoom() {
        Map<HouseColor, Integer> tmp = this.schoolBoard.getDiningRoom();

        for (HouseColor color : HouseColor.values()) assertEquals(0, tmp.get(color));
    }

    /**
     * Tests whether returns the number of students of a specific color in the dining room.
     */
    @Test
    void getStudentsNumberOf() {
        HouseColor color = HouseColor.BLUE;

        assertEquals(0, this.schoolBoard.getStudentsNumberOf(color));
    }

    /**
     * Tests whether adds students to the entrance.
     */
    @Test
    void addToEntrance() {
        Map<HouseColor, Integer> tmp = new EnumMap<>(HouseColor.class);

        for (HouseColor color : HouseColor.values()) tmp.put(color, 1);
        this.schoolBoard.addToEntrance(tmp);
        for (HouseColor color : HouseColor.values()) assertEquals(1, tmp.get(color));
    }

    /**
     * Tests whether removes a student from the entrance.
     *
     * @throws NoStudentException Whether there is no student of that color.
     */
    @Test
    void removeFromEntrance() throws NoStudentException {
        HouseColor color = HouseColor.BLUE;
        Map<HouseColor, Integer> tmp = new EnumMap<>(HouseColor.class);

        for (HouseColor c : HouseColor.values()) tmp.put(c, c.equals(color) ? 1 : 0);
        this.schoolBoard.addToEntrance(tmp);
        this.schoolBoard.removeFromEntrance(color);
        assertEquals(0, this.schoolBoard.getStudentsNumberOf(color));
    }

    /**
     * Tests whether adds a student to the dining room.
     */
    @Test
    void addToDiningRoom() {
        HouseColor color = HouseColor.BLUE;

        this.schoolBoard.addToDiningRoom(color);
        assertEquals(1, this.schoolBoard.getStudentsNumberOf(color));
    }

    /**
     * Tests whether removes a student from the entrance.
     *
     * @throws NoStudentException Whether there is no student of that color.
     */
    @Test
    void removeFromDiningRoom() throws NoStudentException {
        HouseColor color = HouseColor.BLUE;

        this.schoolBoard.addToDiningRoom(color);
        this.schoolBoard.removeFromDiningRoom(color);
        assertEquals(0, this.schoolBoard.getStudentsNumberOf(color));
    }

    /**
     * Tests whether removes towers from the school board.
     */
    @Test
    void removeTowers() throws NotEnoughTowersException, NegativeException {
        int removeTowers = 1;

        this.schoolBoard.removeTowers(removeTowers);
        assertEquals(this.towersNumber - removeTowers, this.schoolBoard.getTowersNumber());
    }

    /**
     * Tests whether adds towers to the school board.
     */
    @Test
    void addTowers() throws NegativeException {
        int addTowers = 1;

        this.schoolBoard.addTowers(addTowers);
        assertEquals(this.towersNumber + addTowers, this.schoolBoard.getTowersNumber());
    }
}