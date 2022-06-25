package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.utilities.HouseColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test of class Cloud.
 *
 * @author Riccardo Milici
 */
public class CloudTest {

    private Cloud cloud;

    /**
     * Generates a new GameBoard.
     */
    @BeforeEach
    void setup() {
        this.cloud = new Cloud(1);
    }

    /**
     * Allows the garbage collector to delete the created Assistant.
     */
    @AfterEach
    void tearDown() {
        this.cloud = null;
    }

    /**
     * Tests if the correct id is returned.
     */
    @Test
    void getId() {
        assertEquals(1, cloud.getId());
    }

    /**
     * Tests if the correct students are returned.
     */
    @Test
    void getStudents() {
        Map<HouseColor, Integer> sampleStudents = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) sampleStudents.put(color, 0);

        assertEquals(sampleStudents, cloud.getStudents());
    }

    /**
     * Tests if an empty cloud is correctly identified.
     */
    @Test
    void isNotFull() {
        assertFalse(cloud.isFull());
    }

    /**
     * Tests if a full cloud is correctly identified.
     */
    @Test
    void isFull() {
        Map<HouseColor, Integer> sampleStudents = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) sampleStudents.put(color, 1);

        cloud.refill(sampleStudents);
        assertTrue(cloud.isFull());
    }

    /**
     * Tests if a cloud refill is correctly performed.
     */
    @Test
    void refill() {
        Map<HouseColor, Integer> sampleStudents = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) sampleStudents.put(color, 1);

        cloud.refill(sampleStudents);
        assertEquals(sampleStudents, cloud.getStudents());
    }

    /**
     * Tests if a cloud flush is correctly performed.
     */
    @Test
    void flush() {
        Map<HouseColor, Integer> sampleStudents = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) sampleStudents.put(color, 1);

        cloud.refill(sampleStudents);
        cloud.flush();
        assertFalse(cloud.isFull());
    }

    /**
     * Tets if a cloud restore is correctly performed.
     */
    @Test
    void cloudRestore() {
        Map<HouseColor, Integer> sampleStudents = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) sampleStudents.put(color, 1);
        this.cloud = new Cloud(1, sampleStudents);

        assertEquals(sampleStudents, cloud.getStudents());
    }

    /**
     * Tests if two identical clouds are recognized.
     */
    @Test
    void cloudsEquals() {
        Map<HouseColor, Integer> sampleStudents = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) sampleStudents.put(color, 1);
        Cloud sampleCloud = new Cloud(1, sampleStudents);
        cloud.refill(sampleStudents);

        assertEquals(cloud, sampleCloud);
    }

    /**
     * Tests if two identical clouds have the same hash code.
     */
    @Test
    void cloudsHash() {
        Map<HouseColor, Integer> sampleStudents = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) sampleStudents.put(color, 1);
        Cloud sampleCloud = new Cloud(1, sampleStudents);
        cloud.refill(sampleStudents);

        assertEquals(cloud.hashCode(), sampleCloud.hashCode());
    }
}
