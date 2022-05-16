package it.polimi.ingsw.server.model.board.effects;

import it.polimi.ingsw.utilities.HouseColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EmptyStackException;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test of class PrincessEffect.
 *
 * @author Riccardo Motta
 */
class PrincessEffectTest {

    PrincessEffect princessEffect;

    /**
     * Generates a new PrincessEffect.
     */
    @BeforeEach
    void setUp() {
        princessEffect = new PrincessEffect();
    }

    /**
     * Allows the garbage collector to delete the created PrincessEffect.
     */
    @AfterEach
    void tearDown() {
        princessEffect = null;
    }

    /**
     * Tests if returns the correct ID.
     */
    @Test
    void getId() {
        assertEquals(11, princessEffect.getId());
    }

    /**
     * Tests if the effect works correctly.
     */
    @Test
    void effect() {
        Map<HouseColor, Integer> map = getMap();
        princessEffect.effect(null, null);
        assertEquals(map, princessEffect.getStudents());
        princessEffect.effect(null, HouseColor.BLUE);
        map.put(HouseColor.BLUE, 1);
        assertEquals(map, princessEffect.getStudents());
        princessEffect.effect(HouseColor.BLUE, null);
        map.put(HouseColor.BLUE, 0);
        assertEquals(map, princessEffect.getStudents());
        try {
            princessEffect.effect(HouseColor.BLUE, HouseColor.BLUE);
            assert false;
        } catch (EmptyStackException e) {
            assert true;
        }
    }

    /**
     * Tests if returns the correct cost.
     */
    @Test
    void getCost() {
        assertEquals(2, princessEffect.getCost());
    }

    /**
     * Tests if the function returns the right data.
     */
    @Test
    void getStudents() {
        assertEquals(getMap(), princessEffect.getStudents());
    }

    /**
     * Generates a test map.
     *
     * @return The generated map.
     */
    private Map<HouseColor, Integer> getMap() {
        Map<HouseColor, Integer> map = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values())
            map.put(color, 0);
        return map;
    }
}