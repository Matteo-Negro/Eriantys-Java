package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.board.effects.MonkEffect;
import it.polimi.ingsw.utilities.HouseColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test of class SpecialCharacter.
 *
 * @author Riccardo Motta
 */
class SpecialCharacterTest {

    private SpecialCharacter specialCharacter;

    /**
     * Generates a new MonkEffect contained into SpecialCharacter (id: 1).
     */
    @BeforeEach
    void setUp() {
        Map<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) students.put(color, 0);
        specialCharacter = new SpecialCharacter(1, students);
    }

    /**
     * Allows the garbage collector to delete the created SpecialCharacter.
     */
    @AfterEach
    void tearDown() {
        specialCharacter = null;
    }

    /**
     * Tests if returns the correct ID.
     */
    @Test
    void getId() {
        assertEquals(1, specialCharacter.getId());
    }

    /**
     * Tests if the effect cost is correct.
     */
    @Test
    void getEffectCost() {
        assertEquals(1, specialCharacter.getEffectCost());
        assertEquals(1, specialCharacter.getEffect().getCost());
    }

    /**
     * Tests if the effect coincides with a fresh MonkEffect.
     */
    @Test
    void getEffect() {
        assertEquals(new MonkEffect(), specialCharacter.getEffect());
    }

    /**
     * Tests if the methods sets correctly the values.
     */
    @Test
    void activateEffect() {
        specialCharacter.activateEffect();
        assertTrue(specialCharacter.isAlreadyPaid());
        assertTrue(specialCharacter.isPaidInRound());
        assertTrue(specialCharacter.isActive());
    }

    /**
     * Tests if the method correctly deactivates the effect.
     */
    @Test
    void cleanEffect() {
        specialCharacter.cleanEffect();
        assertFalse(specialCharacter.isActive());
    }

    /**
     * Tests if the method correctly sets the paidInRound variable to false.
     */
    @Test
    void changedRound() {
        specialCharacter.changedRound();
        assertFalse(specialCharacter.isPaidInRound());
    }
}