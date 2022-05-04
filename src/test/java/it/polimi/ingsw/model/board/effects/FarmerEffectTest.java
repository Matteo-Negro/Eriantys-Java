package it.polimi.ingsw.model.board.effects;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.WizardType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test of class FarmerEffect.
 *
 * @author Riccardo Motta
 */
class FarmerEffectTest {

    FarmerEffect farmerEffect;

    /**
     * Generates a new FarmerEffect.
     */
    @BeforeEach
    void setUp() {
        farmerEffect = new FarmerEffect();
    }

    /**
     * Allows the garbage collector to delete the created FarmerEffect.
     */
    @AfterEach
    void tearDown() {
        farmerEffect = null;
    }

    /**
     * Tests if returns the correct ID.
     */
    @Test
    void getId() {
        assertEquals(2, farmerEffect.getId());
    }

    /**
     * Tests if the effect works correctly.
     */
    @Test
    void effect() {
        farmerEffect.effect(getMap());
        assertEquals(getMap(), farmerEffect.getStolenProfessors());
    }

    /**
     * Tests if returns the correct cost.
     */
    @Test
    void getCost() {
        assertEquals(2, farmerEffect.getCost());
    }

    /**
     * Tests if the returned stolen professors coincide.
     */
    @Test
    void getStolenProfessors() {
        farmerEffect.effect(getMap());
        assertEquals(getMap(), farmerEffect.getStolenProfessors());
    }

    /**
     * Generates a test map.
     *
     * @return The generated map.
     */
    private Map<HouseColor, Player> getMap() {
        EnumMap<HouseColor, Player> map = new EnumMap<>(HouseColor.class);
        map.put(HouseColor.BLUE, new Player("Player 1", WizardType.FUCHSIA, 6, TowerType.WHITE));
        map.put(HouseColor.FUCHSIA, new Player("Player 2", WizardType.WHITE, 6, TowerType.BLACK));
        return map;
    }
}