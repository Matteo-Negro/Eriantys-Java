package it.polimi.ingsw.server.model.board.effects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test of class ThiefEffect.
 *
 * @author Riccardo Motta
 */
class ThiefEffectTest {

    ThiefEffect thiefEffect;

    /**
     * Generates a new ThiefEffect.
     */
    @BeforeEach
    void setUp() {
        thiefEffect = new ThiefEffect();
    }

    /**
     * Allows the garbage collector to delete the created ThiefEffect.
     */
    @AfterEach
    void tearDown() {
        thiefEffect = null;
    }

    /**
     * Tests if returns the correct ID.
     */
    @Test
    void getId() {
        assertEquals(12, thiefEffect.getId());
    }

    /**
     * Tests if returns the correct cost.
     */
    @Test
    void getCost() {
        assertEquals(3, thiefEffect.getCost());
    }
}