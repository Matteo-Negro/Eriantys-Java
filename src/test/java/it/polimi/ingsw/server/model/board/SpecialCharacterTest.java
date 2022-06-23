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
     * Tries to build every possible effect.
     */
    @Test
    void constructorsTest() {
        Map<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);

        equals(new SpecialCharacter(1, students), new SpecialCharacter(1, 1, false, false, false, students, 0, 0));
        equals(new SpecialCharacter(5, students), new SpecialCharacter(5, 2, false, false, false, students, 4, 0));
        equals(new SpecialCharacter(7, students), new SpecialCharacter(7, 1, false, false, false, students, 0, 0));
        equals(new SpecialCharacter(11, students), new SpecialCharacter(11, 2, false, false, false, students, 0, 0));
        for (int index = 1; index <= 12; index++)
            equals(index);
        try {
            new SpecialCharacter(13, null);
            assert false;
        } catch (IllegalStateException e) {
            assert true;
        }
    }

    /**
     * Utility function for testing the equality of two special characters.
     *
     * @param specialCharacter1 First special character.
     * @param specialCharacter2 Second special character.
     */
    private void equals(SpecialCharacter specialCharacter1, SpecialCharacter specialCharacter2) {
        assertEquals(specialCharacter1, specialCharacter2);
        assertEquals(specialCharacter1.hashCode(), specialCharacter2.hashCode());
    }

    /**
     * Utility function for testing the equality of two special characters according to id.
     *
     * @param id The id of the special character to test.
     */
    private void equals(int id) {
        assertEquals(id, new SpecialCharacter(id, new EnumMap<>(HouseColor.class)).getId());
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
        assertTrue(specialCharacter.isPaidInTurn());
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
    void changedTurn() {
        specialCharacter.changedTurn();
        assertFalse(specialCharacter.isPaidInTurn());
    }
}