package it.polimi.ingsw.server.model.board.effects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test of class HeraldEffect.
 *
 * @author Riccardo Motta
 */
class HeraldEffectTest {

    HeraldEffect heraldEffect;

    /**
     * Generates a new HeraldEffect.
     */
    @BeforeEach
    void setUp() {
        heraldEffect = new HeraldEffect();
    }

    /**
     * Allows the garbage collector to delete the created HeraldEffect.
     */
    @AfterEach
    void tearDown() {
        heraldEffect = null;
    }

    /**
     * Tests if returns the correct ID.
     */
    @Test
    void getId() {
        assertEquals(3, heraldEffect.getId());
    }

    /**
     * Tests if returns the correct cost.
     */
    @Test
    void getCost() {
        assertEquals(3, heraldEffect.getCost());
    }
}