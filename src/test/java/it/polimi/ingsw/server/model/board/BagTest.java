package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.utilities.HouseColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test of class Bag.
 *
 * @author Riccardo Milici
 */
public class BagTest {

    private Bag bag;

    /**
     * Generates a new GameBoard.
     */
    @BeforeEach
    void setUp() {
        this.bag = new Bag();
    }

    /**
     * Allows the garbage collector to delete the created Assistant.
     */
    @AfterEach
    void TearDown() {
        this.bag = null;
    }

    /**
     * Tests if the pop is performed correctly.
     */
    @Test
    void pop() {
        assertNotNull(bag.pop());
    }

    /**
     * Tests if the correct status is returned.
     */
    @Test
    void getStatus() {
        Map<HouseColor, Integer> sampleContainedStudents = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) sampleContainedStudents.put(color, 24);

        assertEquals(sampleContainedStudents, bag.getStatus());
    }

    /**
     * Tests if the push is performed correctly.
     */
    @Test
    void push() {
        Map<HouseColor, Integer> sampleContainedStudents = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) sampleContainedStudents.put(color, 24);
        sampleContainedStudents.replace(HouseColor.BLUE, 25);

        bag.push(HouseColor.BLUE);
        assertEquals(sampleContainedStudents, bag.getStatus());
    }

    /**
     * Tests if the board setup is performed correctly.
     */
    @Test
    void boardSetup() {
        List<HouseColor> setUp = bag.boardSetUp();

        assertTrue(setUp.size() == 10 && setUp.containsAll(Arrays.stream(HouseColor.values()).toList()));
    }

    /**
     * Tests if a not empty bag is correctly identified.
     */
    @Test
    void isNotEmpty() {
        assertFalse(bag.isEmpty());
    }

    /**
     * Tests if an empty bag is correctly identified.
     */
    @Test
    void isEmpty() {
        for (int i = 0; i < 120; i++) bag.pop();

        assertTrue(bag.isEmpty());
    }

    /**
     * Tests if a restore is correctly performed.
     */
    @Test
    void bagRestore() {
        Map<HouseColor, Integer> sampleContainedStudents = new EnumMap<>(HouseColor.class);
        sampleContainedStudents.put(HouseColor.RED, 20);
        sampleContainedStudents.put(HouseColor.BLUE, 0);
        sampleContainedStudents.put(HouseColor.YELLOW, 15);
        sampleContainedStudents.put(HouseColor.GREEN, 22);
        sampleContainedStudents.put(HouseColor.FUCHSIA, 4);

        this.bag = new Bag(sampleContainedStudents);
        sampleContainedStudents.remove(HouseColor.BLUE);

        assertEquals(sampleContainedStudents, bag.getStatus());
    }

    /**
     * Tests if two bags with the same content are correctly identified.
     */
    @Test
    void bagsEquals() {
        Bag sampleBag = new Bag();

        assertEquals(sampleBag.getStatus(), bag.getStatus());
        assertNotEquals(sampleBag, bag);
    }

    /**
     * Tests if two bags with the same content have different hash codes due to the different students order into the stack.
     */
    @Test
    void bagsHash() {
        Bag sampleBag = new Bag();

        assertNotEquals(sampleBag.hashCode(), bag.hashCode());
    }
}
