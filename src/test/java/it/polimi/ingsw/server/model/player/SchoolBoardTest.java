package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.exceptions.NegativeException;
import it.polimi.ingsw.utilities.exceptions.NoStudentException;
import it.polimi.ingsw.utilities.exceptions.NotEnoughTowersException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test of class SchoolBoard.
 *
 * @author Matteo Negro
 */
class SchoolBoardTest {

    private final int towersNumber = 8;
    private final Map<HouseColor, Integer> entrance = new EnumMap<>(HouseColor.class);
    private final Map<HouseColor, Integer> diningRoom = new EnumMap<>(HouseColor.class);

    private List<SchoolBoard> schoolBoards;

    /**
     * Generates a new SchoolBoard.
     */
    @BeforeEach
    void setUp() {
        schoolBoards = new ArrayList<>();

        for (HouseColor color : HouseColor.values()) this.entrance.put(color, 1);
        for (HouseColor color : HouseColor.values()) this.diningRoom.put(color, 1);
        this.schoolBoards.add(new SchoolBoard(this.towersNumber, TowerType.BLACK, this.entrance));
        this.schoolBoards.add(new SchoolBoard(this.towersNumber, TowerType.WHITE, this.diningRoom, this.entrance));
    }

    /**
     * Allows the garbage collector to delete the created Assistant.
     */
    @AfterEach
    void tearDown() {
        this.schoolBoards = null;
    }

    /**
     * Tests whether returns the number of towers.
     */
    @Test
    void getTowersNumber() {
        assertEquals(this.towersNumber, this.schoolBoards.get(0).getTowersNumber());
        assertEquals(this.towersNumber, this.schoolBoards.get(1).getTowersNumber());
    }

    /**
     * Tests whether returns the color of the tower.
     */
    @Test
    void getTowerType() {
        assertEquals(TowerType.BLACK, this.schoolBoards.get(0).getTowerType());
        assertEquals(TowerType.WHITE, this.schoolBoards.get(1).getTowerType());
    }

    /**
     * Tests whether returns the list of students in the entrance.
     */
    @Test
    void getEntrance() {
        Map<HouseColor, Integer> tmp = this.schoolBoards.get(0).getEntrance();

        for (HouseColor color : HouseColor.values()) assertEquals(1, tmp.get(color));
    }

    /**
     * Tests whether returns the list of students in the dining room.
     */
    @Test
    void getDiningRoom() {
        Map<HouseColor, Integer> tmp;

        tmp = this.schoolBoards.get(0).getDiningRoom();
        for (HouseColor color : HouseColor.values()) assertEquals(0, tmp.get(color));

        tmp = this.schoolBoards.get(1).getDiningRoom();
        for (HouseColor color : HouseColor.values()) assertEquals(1, tmp.get(color));
    }

    /**
     * Tests whether returns the number of students of a specific color in the dining room.
     */
    @Test
    void getStudentsNumberOf() {
        HouseColor color = HouseColor.BLUE;

        assertEquals(0, this.schoolBoards.get(0).getStudentsNumberOf(color));
        assertEquals(1, this.schoolBoards.get(1).getStudentsNumberOf(color));
    }

    /**
     * Tests whether adds students to the entrance.
     */
    @Test
    void addToEntrance() {
        Map<HouseColor, Integer> tmp = new EnumMap<>(HouseColor.class);

        for (HouseColor color : HouseColor.values()) tmp.put(color, 1);
        this.schoolBoards.get(0).addToEntrance(tmp);
        for (HouseColor color : HouseColor.values()) assertEquals(2, this.schoolBoards.get(0).getEntrance().get(color));
        this.schoolBoards.get(1).addToEntrance(tmp);
        for (HouseColor color : HouseColor.values()) assertEquals(2, this.schoolBoards.get(1).getEntrance().get(color));
    }

    /**
     * Tests whether removes a student from the entrance.
     */
    @Test
    void removeFromEntrance() {
        HouseColor color = HouseColor.BLUE;

        try {
            this.schoolBoards.get(0).removeFromEntrance(color);
            assertEquals(0, this.schoolBoards.get(0).getStudentsNumberOf(color));
            this.schoolBoards.get(1).removeFromEntrance(color);
            assertEquals(0, this.schoolBoards.get(1).getEntrance().get(color));
        } catch (NoStudentException e) {
            assert false;
        }
        try {
            this.schoolBoards.get(1).removeFromEntrance(color);
        } catch (NoStudentException e) {
            assert true;
        }
    }

    /**
     * Tests whether adds a student to the dining room.
     */
    @Test
    void addToDiningRoom() {
        HouseColor color = HouseColor.BLUE;

        this.schoolBoards.get(0).addToDiningRoom(color);
        assertEquals(1, this.schoolBoards.get(0).getStudentsNumberOf(color));
        this.schoolBoards.get(1).addToDiningRoom(color);
        assertEquals(2, this.schoolBoards.get(1).getStudentsNumberOf(color));
    }

    /**
     * Tests whether removes a student from the entrance.
     */
    @Test
    void removeFromDiningRoom() {
        HouseColor color = HouseColor.BLUE;
        try {
            this.schoolBoards.get(0).addToDiningRoom(color);
            this.schoolBoards.get(0).removeFromDiningRoom(color);
            assertEquals(0, this.schoolBoards.get(0).getStudentsNumberOf(color));

            this.schoolBoards.get(1).removeFromDiningRoom(color);
            assertEquals(0, this.schoolBoards.get(1).getStudentsNumberOf(color));
        } catch (NoStudentException e) {
            assert false;
        }
        try {
            this.schoolBoards.get(1).removeFromDiningRoom(color);
        } catch (NoStudentException e) {
            assert true;
        }
    }

    /**
     * Tests whether removes towers from the school board.
     */
    @Test
    void removeTowers() throws NotEnoughTowersException, NegativeException {
        int removeTowers = 1;

        this.schoolBoards.get(0).removeTowers(removeTowers);
        assertEquals(this.towersNumber - removeTowers, this.schoolBoards.get(0).getTowersNumber());
        this.schoolBoards.get(1).removeTowers(removeTowers);
        assertEquals(this.towersNumber - removeTowers, this.schoolBoards.get(1).getTowersNumber());
        try {
            for (int i = 1; i <= this.towersNumber; i++) this.schoolBoards.get(1).removeTowers(removeTowers);
        } catch (NotEnoughTowersException e) {
            assert true;
        }
    }

    /**
     * Tests whether adds towers to the school board.
     */
    @Test
    void addTowers() throws NegativeException {
        int addTowers = 1;

        this.schoolBoards.get(0).addTowers(addTowers);
        assertEquals(this.towersNumber + addTowers, this.schoolBoards.get(0).getTowersNumber());
        this.schoolBoards.get(1).addTowers(addTowers);
        assertEquals(this.towersNumber + addTowers, this.schoolBoards.get(1).getTowersNumber());
    }
}