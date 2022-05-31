package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.board.SpecialCharacter;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.WizardType;
import it.polimi.ingsw.utilities.exceptions.AlreadyPlayedException;
import it.polimi.ingsw.utilities.exceptions.NotEnoughCoinsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test of class Player.
 *
 * @author matteo Negro
 */
class PlayerTest {

    private final int towersNumber = 8;
    private final String name = "Matteo";
    private final TowerType towerType = TowerType.BLACK;
    private final WizardType wizardType = WizardType.YELLOW;
    private final Map<HouseColor, Integer> entrance = new EnumMap<>(HouseColor.class);

    private Player player;

    /**
     * Generates a new Player.
     */
    @BeforeEach
    void setUp() {
        this.player = new Player(this.name, this.wizardType, this.towersNumber, this.towerType, this.entrance);
    }

    /**
     * Allows the garbage collector to delete the created Player.
     */
    @AfterEach
    void tearDown() {
        this.player = null;
    }

    /**
     * Tests whether returns the name of the player.
     */
    @Test
    void getName() {
        assertEquals(this.name, this.player.getName());
    }

    /**
     * Tests whether returns the wizard type of the player.
     */
    @Test
    void getWizard() {
        assertEquals(this.wizardType, this.player.getWizard());
    }

    /**
     * Tests whether returns the coins that are owns by the player.
     */
    @Test
    void getCoins() {
        assertEquals(1, this.player.getCoins());
    }

    /**
     * Tests whether adds coins to the player.
     */
    @Test
    void addCoins() {
        this.player.addCoins();
        assertEquals(2, this.player.getCoins());
    }

    /**
     * Tests whether returns the list of the remaining assistants in the player's hand.
     */
    @Test
    void getAssistants() {
        List<Assistant> tmp = this.player.getAssistants();

        assertEquals(10, tmp.size());
        for (int index = 1; index <= 10; index++) assertEquals(index, tmp.get(index - 1).getId());
    }

    /**
     * Tests whether returns the assistant that the player played.
     *
     * @throws AlreadyPlayedException Whether the Assistant had already been played.
     */
    @Test
    void playAssistant() throws AlreadyPlayedException {
        int playedAssistantID = 1;
        Assistant tmp = this.player.playAssistant(playedAssistantID);

        assertEquals(playedAssistantID, tmp.getId());
        assertFalse(this.player.getAssistants().contains(tmp));
    }

    /**
     * Tests whether the player pays for the special character and whether it will be active.
     *
     * @throws NotEnoughCoinsException Whether the number of available coins is less than the required one.
     */
    @Test
    void paySpecialCharacter() throws NotEnoughCoinsException {
        int cost = 1;
        int selectedSpecialCharacterID = 1;
        Map<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);

        for (HouseColor c : HouseColor.values()) students.put(c, 1);
        SpecialCharacter tmp = new SpecialCharacter(selectedSpecialCharacterID, students);

        for (int coin = 1; coin <= cost; coin++) this.player.addCoins();
        this.player.paySpecialCharacter(tmp);
        assertEquals(1, this.player.getCoins());
        assertTrue(tmp.isActive());
    }

    /**
     * Tests whether returns the school board of the player.
     */
    @Test
    void getSchoolBoard() {
        SchoolBoard tmp = this.player.getSchoolBoard();

        assertEquals(this.towerType, tmp.getTowerType());
        assertEquals(this.towersNumber, tmp.getTowersNumber());
    }
}