package it.polimi.ingsw.server.model.player;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test of class Assistant.
 *
 * @author Matteo Negro
 */
class AssistantTest {

    private List<Assistant> assistants;

    /**
     * Generates a new Assistant.
     */
    @BeforeEach
    void setUp() {
        this.assistants = new ArrayList<>();
        for (int i = 1; i <= 8; i++) this.assistants.add((i % 2 == 0) ? new Assistant(i) : new Assistant(i, true));
        try {
            this.assistants.add(new Assistant(11, true));
        } catch (IndexOutOfBoundsException e) {
            this.assistants.add(new Assistant(9, true));
        }
        try {
            this.assistants.add(new Assistant(12));
        } catch (IndexOutOfBoundsException e) {
            this.assistants.add(new Assistant(10));
        }
    }

    /**
     * Allows the garbage collector to delete the created Assistant.
     */
    @AfterEach
    void tearDown() {
        this.assistants = null;
    }

    /**
     * Tests whether returns the ID of the card.
     */
    @Test
    void getId() {
        for (int i = 1; i <= 10; i++) assertEquals(i, this.assistants.get(i - 1).getId());
    }

    /**
     * Test whether the assistant a has the bonus.
     */
    @Test
    void hasBonus() {
        for (int i = 1; i <= 10; i++) assertEquals(!(i % 2 == 0), this.assistants.get(i - 1).hasBonus());
    }

    /**
     * Tests whether returns the right distance links to the ID of card.
     */
    @Test
    void getMaxDistance() {
        for (int i = 1; i <= 10; i++)
            assertEquals((i % 2 == 0) ? (i - 1) / 2 + 1 : (i - 1) / 2 + 3, this.assistants.get(i - 1).getMaxDistance());
    }

    /**
     * Tests whether sets the bonus and increases the max distance.
     */
    @Test
    void setBonus() {
        for (int i = 1; i <= 10; i++) if (i % 2 == 0) this.assistants.get(i - 1).setBonus();
        for (int i = 1; i <= 10; i++) assertEquals((i - 1) / 2 + 3, this.assistants.get(i - 1).getMaxDistance());

    }
}