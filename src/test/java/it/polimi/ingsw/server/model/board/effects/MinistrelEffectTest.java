package it.polimi.ingsw.server.model.board.effects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test of class MinistrelEffect.
 *
 * @author Riccardo Motta
 */
class MinistrelEffectTest {

    MinstrelEffect minstrelEffect;

    /**
     * Generates a new MinistrelEffect.
     */
    @BeforeEach
    void setUp() {
        minstrelEffect = new MinstrelEffect();
    }

    /**
     * Allows the garbage collector to delete the created MinistrelEffect.
     */
    @AfterEach
    void tearDown() {
        minstrelEffect = null;
    }

    /**
     * Tests if returns the correct ID.
     */
    @Test
    void getId() {
        assertEquals(10, minstrelEffect.getId());
    }

    /**
     * Tests if returns the correct cost.
     */
    @Test
    void getCost() {
        assertEquals(1, minstrelEffect.getCost());
    }
}