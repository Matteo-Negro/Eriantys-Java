package it.polimi.ingsw.server.model.board.effects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test of class KnightEffect.
 *
 * @author Riccardo Motta
 */
class KnightEffectTest {

    KnightEffect knightEffect;

    /**
     * Generates a new KnightEffect.
     */
    @BeforeEach
    void setUp() {
        knightEffect = new KnightEffect();
    }

    /**
     * Allows the garbage collector to delete the created KnightEffect.
     */
    @AfterEach
    void tearDown() {
        knightEffect = null;
    }

    /**
     * Tests if returns the correct ID.
     */
    @Test
    void getId() {
        assertEquals(8, knightEffect.getId());
    }

    /**
     * Tests if returns the correct cost.
     */
    @Test
    void getCost() {
        assertEquals(2, knightEffect.getCost());
    }
}