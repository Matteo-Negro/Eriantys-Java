package it.polimi.ingsw.server.model.player;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test of class Assistant.
 *
 * @author Matteo Negro
 */
class AssistantTest {


    private final int id = 10;

    private Assistant assistant;

    /**
     * Generates a new Assistant.
     */
    @BeforeEach
    void setUp() {
        this.assistant = new Assistant(this.id);
    }

    /**
     * Allows the garbage collector to delete the created Assistant.
     */
    @AfterEach
    void tearDown() {
        this.assistant = null;
    }

    /**
     * Tests whether returns the ID of the card.
     */
    @Test
    void getId() {
        assertEquals(this.id, this.assistant.getId());
    }

    /**
     *  Test whether the assistant a has the bonus.
     */
    @Test
    void hasBonus() {
        this.assistant.setBonus();
        assertTrue(this.assistant.hasBonus());
    }
    /**
     * Tests whether returns the right distance links to the ID of card.
     */
    @Test
    void getMaxDistance() {
        assertEquals(this.id / 2 + 1, this.assistant.getMaxDistance());
    }

    /**
     * Tests whether sets the bonus and increases the max distance.
     */
    @Test
    void setBonus() {
        this.assistant.setBonus();
        assertEquals(this.id / 2 + 3, this.assistant.getMaxDistance());
    }
}