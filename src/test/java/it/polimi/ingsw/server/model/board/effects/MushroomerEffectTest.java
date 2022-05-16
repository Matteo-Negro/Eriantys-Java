package it.polimi.ingsw.server.model.board.effects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test of class MushroomerEffect.
 *
 * @author Riccardo Motta
 */
class MushroomerEffectTest {

    MushroomerEffect mushroomerEffect;

    /**
     * Generates a new MushroomerEffect.
     */
    @BeforeEach
    void setUp() {
        mushroomerEffect = new MushroomerEffect();
    }

    /**
     * Allows the garbage collector to delete the created MushroomerEffect.
     */
    @AfterEach
    void tearDown() {
        mushroomerEffect = null;
    }

    /**
     * Tests if returns the correct ID.
     */
    @Test
    void getId() {
        assertEquals(9, mushroomerEffect.getId());
    }

    /**
     * Tests if returns the correct cost.
     */
    @Test
    void getCost() {
        assertEquals(3, mushroomerEffect.getCost());
    }
}