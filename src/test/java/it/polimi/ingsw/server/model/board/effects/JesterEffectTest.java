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
 * Test of class JesterEffect.
 *
 * @author Riccardo Motta
 */
class JesterEffectTest {

    JesterEffect jesterEffect;

    /**
     * Generates a new JesterEffect.
     */
    @BeforeEach
    void setUp() {
        jesterEffect = new JesterEffect();
    }

    /**
     * Allows the garbage collector to delete the created JesterEffect.
     */
    @AfterEach
    void tearDown() {
        jesterEffect = null;
    }

    /**
     * Tests if returns the correct ID.
     */
    @Test
    void getId() {
        assertEquals(7, jesterEffect.getId());
    }

    /**
     * Tests if the effect works correctly.
     */
    @Test
    void effect() {
        Map<HouseColor, Integer> map = getMap();
        jesterEffect.effect(null, null);
        assertEquals(map, jesterEffect.getStudents());
        jesterEffect.effect(null, HouseColor.BLUE);
        map.put(HouseColor.BLUE, 1);
        assertEquals(map, jesterEffect.getStudents());
        jesterEffect.effect(HouseColor.BLUE, null);
        map.put(HouseColor.BLUE, 0);
        assertEquals(map, jesterEffect.getStudents());
        try {
            jesterEffect.effect(HouseColor.BLUE, HouseColor.BLUE);
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
        assertEquals(1, jesterEffect.getCost());
    }

    /**
     * Tests if the function returns the right data.
     */
    @Test
    void getStudents() {
        assertEquals(getMap(), jesterEffect.getStudents());
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