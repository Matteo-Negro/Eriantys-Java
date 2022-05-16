package it.polimi.ingsw.server.model.board.effects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test of class MessengerEffect.
 *
 * @author Riccardo Motta
 */
class MessengerEffectTest {

    MessengerEffect messengerEffect;

    /**
     * Generates a new MessengerEffect.
     */
    @BeforeEach
    void setUp() {
        messengerEffect = new MessengerEffect();
    }

    /**
     * Allows the garbage collector to delete the created MessengerEffect.
     */
    @AfterEach
    void tearDown() {
        messengerEffect = null;
    }

    /**
     * Tests if returns the correct ID.
     */
    @Test
    void getId() {
        assertEquals(4, messengerEffect.getId());
    }

    /**
     * Tests if returns the correct cost.
     */
    @Test
    void getCost() {
        assertEquals(1, messengerEffect.getCost());
    }
}