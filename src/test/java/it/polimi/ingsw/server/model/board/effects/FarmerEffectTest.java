package it.polimi.ingsw.server.model.board.effects;

import it.polimi.ingsw.server.model.player.Player;
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
     * Tests if returns the correct cost.
     */
    @Test
    void getCost() {
        assertEquals(2, farmerEffect.getCost());
    }
}