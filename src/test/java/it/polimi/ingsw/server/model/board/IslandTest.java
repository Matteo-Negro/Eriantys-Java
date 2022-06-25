package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test of class Island.
 *
 * @author Riccardo Milici
 */
public class IslandTest {
    private final int id = 1;
    private final HouseColor student = HouseColor.BLUE;

    private Island island;

    /**
     * Generates a new GameBoard.
     */
    @BeforeEach
    void setUp() {
        this.island = new Island(student, id);
    }

    /**
     * Allows the garbage collector to delete the created Assistant.
     */
    @AfterEach
    void tearDown() {
        this.island = null;
    }

    /**
     * Tests if the correct id is returned.
     */
    @Test
    void getId() {
        assertEquals(this.id, island.getId());
    }

    /**
     * Tests if the correct size is returned.
     */
    @Test
    void getSize() {
        assertEquals(1, island.getSize());
    }

    /**
     * Tests if the correct tower is returned.
     */
    @Test
    void getTower() {
        assertNull(island.getTower());
    }

    /**
     * Tests if the correct students are returned.
     */
    @Test
    void getStudents() {
        Map<HouseColor, Integer> students = new EnumMap<HouseColor, Integer>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) students.put(color, 0);

        students.replace(this.student, 1);

        assertEquals(students, island.getStudents());
    }

    /**
     * Tests if the correct second of the ban is returned.
     */
    @Test
    void isBanned() {
        assertFalse(island.isBanned());
    }

    /**
     * Tests if the size is set correctly.
     */
    @Test
    void setSize() {
        island.setSize(4);
        assertEquals(4, island.getSize());
    }

    /**
     * Tests if the tower is set correctly.
     */
    @Test
    void setTower() {
        island.setTower(TowerType.WHITE);
        assertEquals(TowerType.WHITE, island.getTower());
    }

    /**
     * Tests if the student is added correctly.
     */
    @Test
    void addStudent() {
        Map<HouseColor, Integer> students = new EnumMap<HouseColor, Integer>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) students.put(color, 0);
        students.replace(HouseColor.BLUE, 2);
        students.replace(HouseColor.YELLOW, 1);
        students.replace(HouseColor.RED, 1);
        students.replace(HouseColor.GREEN, 1);
        students.replace(HouseColor.FUCHSIA, 1);

        island.addStudent(HouseColor.BLUE);
        island.addStudent(HouseColor.YELLOW);
        island.addStudent(HouseColor.GREEN);
        island.addStudent(HouseColor.RED);
        island.addStudent(HouseColor.FUCHSIA);
        assertEquals(students, island.getStudents());
    }

    /**
     * Tests if the ban is set correctly.
     */
    @Test
    void setBan() {
        island.setBan();
        assertTrue(island.isBanned());
    }

    /**
     * Tests if the ban is removed correctly.
     */
    @Test
    void removeBan() {
        island.setBan();
        island.removeBan();
        assertFalse(island.isBanned());
    }

    /**
     * Tests if the island restore is performed correctly.
     */
    @Test
    void restoreIsland() {
        Map<HouseColor, Integer> sampleContainedStudents = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) sampleContainedStudents.put(color, 4);
        int sampleId = 4;
        int sampleSize = 4;
        boolean sampleBan = true;
        TowerType sampleTower = TowerType.WHITE;

        Island sampleIsland = new Island(sampleContainedStudents, sampleId, sampleSize, sampleBan, sampleTower);

        assertEquals(sampleContainedStudents, sampleIsland.getStudents());
        assertEquals(sampleId, sampleIsland.getId());
        assertEquals(sampleSize, sampleIsland.getSize());
        assertEquals(sampleBan, sampleIsland.isBanned());
        assertEquals(sampleTower, sampleIsland.getTower());
    }

    /**
     * Tests if two identical islands are identified correctly.
     */
    @Test
    void islandsEquals() {
        Island sampleIsland = new Island(HouseColor.BLUE, 1);
        sampleIsland.setTower(TowerType.WHITE);
        island.setTower(TowerType.WHITE);
        assertEquals(island, sampleIsland);
    }

    /**
     * Tests if two identical islands have the same hash code.
     */
    @Test
    void islandsHash() {
        Island sampleIsland = new Island(HouseColor.BLUE, 1);
        assertEquals(sampleIsland.hashCode(), island.hashCode());
    }
}
