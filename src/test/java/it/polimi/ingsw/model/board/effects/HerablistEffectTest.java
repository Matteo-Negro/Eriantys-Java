package it.polimi.ingsw.model.board.effects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test of class HerbalistEffect.
 *
 * @author Riccardo Motta
 */
class HerablistEffectTest {

    HerablistEffect herablistEffect;

    /**
     * Generates a new HerbalistEffect.
     */
    @BeforeEach
    void setUp() {
        herablistEffect = new HerablistEffect();
    }

    /**
     * Allows the garbage collector to delete the created HerbalistEffect.
     */
    @AfterEach
    void tearDown() {
        herablistEffect = null;
    }

    /**
     * Tests if returns the correct ID.
     */
    @Test
    void getId() {
        assertEquals(5, herablistEffect.getId());
    }

    /**
     * Tests if the effect works correctly.
     */
    @Test
    void effect() {
        herablistEffect.effect("take");
        assertEquals(4, herablistEffect.getAvailableBans());
        herablistEffect.effect("restore");
        assertEquals(5, herablistEffect.getAvailableBans());
    }

    /**
     * Tests if returns the correct cost.
     */
    @Test
    void getCost() {
        assertEquals(2, herablistEffect.getCost());
    }

    /**
     * Tests if the number of available beans is correct.
     */
    @Test
    void getAvailableBans() {
        assertEquals(5, herablistEffect.getAvailableBans());
    }
}