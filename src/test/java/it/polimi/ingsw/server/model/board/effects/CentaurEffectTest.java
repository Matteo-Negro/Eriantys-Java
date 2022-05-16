package it.polimi.ingsw.server.model.board.effects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test of class CentaurEffect.
 *
 * @author Riccardo Motta
 */
class CentaurEffectTest {

    CentaurEffect centaurEffect;

    /**
     * Generates a new CentaurEffect.
     */
    @BeforeEach
    void setUp() {
        centaurEffect = new CentaurEffect();
    }

    /**
     * Allows the garbage collector to delete the created CentaurEffect.
     */
    @AfterEach
    void tearDown() {
        centaurEffect = null;
    }

    /**
     * Tests if returns the correct ID.
     */
    @Test
    void getId() {
        assertEquals(6, centaurEffect.getId());
    }

    /**
     * Tests if returns the correct cost.
     */
    @Test
    void getCost() {
        assertEquals(3, centaurEffect.getCost());
    }
}