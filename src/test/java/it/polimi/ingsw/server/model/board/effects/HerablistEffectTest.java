package it.polimi.ingsw.server.model.board.effects;

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

    HerbalistEffect herbalistEffect;

    /**
     * Generates a new HerbalistEffect.
     */
    @BeforeEach
    void setUp() {
        herbalistEffect = new HerbalistEffect();
    }

    /**
     * Allows the garbage collector to delete the created HerbalistEffect.
     */
    @AfterEach
    void tearDown() {
        herbalistEffect = null;
    }

    /**
     * Tests if returns the correct ID.
     */
    @Test
    void getId() {
        assertEquals(5, herbalistEffect.getId());
    }

    /**
     * Tests if the effect works correctly.
     */
    @Test
    void effect() {
        herbalistEffect.effect("take");
        assertEquals(4, herbalistEffect.getAvailableBans());
        herbalistEffect.effect("restore");
        assertEquals(5, herbalistEffect.getAvailableBans());
    }

    /**
     * Tests if returns the correct cost.
     */
    @Test
    void getCost() {
        assertEquals(2, herbalistEffect.getCost());
    }

    /**
     * Tests if the number of available beans is correct.
     */
    @Test
    void getAvailableBans() {
        assertEquals(5, herbalistEffect.getAvailableBans());
    }
}