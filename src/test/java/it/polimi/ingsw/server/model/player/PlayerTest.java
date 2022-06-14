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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test of class Player.
 *
 * @author matteo Negro
 */
class PlayerTest {
    private final Map<HouseColor, Integer> entrance = new EnumMap<>(HouseColor.class);

    private List<Player> players;

    /**
     * Generates a new Player.
     */
    @BeforeEach
    void setUp() {
        List<Assistant> assistants = new ArrayList<>();
        players = new ArrayList<>();

        for (int i = 1; i <= 4; i++) assistants.add(new Assistant(i));
        Arrays.stream(HouseColor.values()).forEach(color -> entrance.put(color, !color.equals(HouseColor.YELLOW) ? 2 : 1));

        this.players.add(new Player("Matteo", WizardType.YELLOW, 6, TowerType.BLACK, this.entrance));
        this.players.add(new Player("Motta", WizardType.WHITE, 6, TowerType.WHITE, this.entrance));
        this.players.add(new Player("Milici", WizardType.GREEN, assistants, 0, new SchoolBoard(6, TowerType.GREY, this.entrance)));
    }

    /**
     * Allows the garbage collector to delete the created Player.
     */
    @AfterEach
    void tearDown() {
        this.players = null;
    }

    /**
     * Tests whether returns the name of the player.
     */
    @Test
    void getName() {
        assertEquals("Matteo", this.players.get(0).getName());
        assertEquals("Motta", this.players.get(1).getName());
        assertEquals("Milici", this.players.get(2).getName());
    }

    /**
     * Tests whether returns the wizard type of the player.
     */
    @Test
    void getWizard() {
        assertEquals(WizardType.YELLOW, this.players.get(0).getWizard());
        assertEquals(WizardType.WHITE, this.players.get(1).getWizard());
        assertEquals(WizardType.GREEN, this.players.get(2).getWizard());
    }

    /**
     * Tests whether returns the coins that are owns by the player.
     */
    @Test
    void getCoins() {
        assertEquals(1, this.players.get(0).getCoins());
        assertEquals(1, this.players.get(1).getCoins());
        assertEquals(0, this.players.get(2).getCoins());
    }

    /**
     * Tests whether adds coins to the player.
     */
    @Test
    void addCoins() {
        for (Player p : this.players) p.addCoins();
        assertEquals(2, this.players.get(0).getCoins());
        assertEquals(2, this.players.get(1).getCoins());
        assertEquals(1, this.players.get(2).getCoins());
    }

    /**
     * Tests whether returns the list of the remaining assistants in the player's hand.
     */
    @Test
    void getAssistants() {
        List<Assistant> tmp;

        tmp = this.players.get(0).getAssistants();
        assertEquals(10, tmp.size());
        for (int index = 1; index <= 10; index++) assertEquals(index, tmp.get(index - 1).getId());

        tmp = this.players.get(1).getAssistants();
        assertEquals(10, tmp.size());
        for (int index = 1; index <= 10; index++) assertEquals(index, tmp.get(index - 1).getId());

        tmp = this.players.get(2).getAssistants();
        assertEquals(4, tmp.size());
        for (int index = 1; index <= 4; index++) assertEquals(index, tmp.get(index - 1).getId());
    }

    /**
     * Tests whether returns the assistant that the player played.
     */
    @Test
    void playAssistant() {
        int playedAssistantID = 1;
        Assistant tmp;

        try {
            tmp = this.players.get(0).playAssistant(playedAssistantID);
            assertEquals(playedAssistantID, tmp.getId());
            assertFalse(this.players.get(0).getAssistants().contains(tmp));

            tmp = this.players.get(1).playAssistant(playedAssistantID);
            assertEquals(playedAssistantID, tmp.getId());
            assertFalse(this.players.get(1).getAssistants().contains(tmp));

            tmp = this.players.get(2).playAssistant(playedAssistantID);
            assertEquals(playedAssistantID, tmp.getId());
            assertFalse(this.players.get(2).getAssistants().contains(tmp));
        } catch (AlreadyPlayedException e) {
            assert false;
        }
    }

    /**
     * Tests whether the player pays for the special character and whether it will be active.
     */
    @Test
    void paySpecialCharacter() {
        int cost = 1;
        int selectedSpecialCharacterID = 1;
        Map<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);

        for (HouseColor c : HouseColor.values()) students.put(c, 1);
        SpecialCharacter tmp;

        try {
            tmp = new SpecialCharacter(selectedSpecialCharacterID, students);
            for (int coin = 1; coin <= cost; coin++) this.players.get(0).addCoins();
            this.players.get(0).paySpecialCharacter(tmp);
            assertEquals(1, this.players.get(0).getCoins());
            assertTrue(tmp.isActive());

            tmp = new SpecialCharacter(selectedSpecialCharacterID, students);
            for (int coin = 1; coin <= cost; coin++) this.players.get(1).addCoins();
            this.players.get(1).paySpecialCharacter(tmp);
            assertEquals(1, this.players.get(1).getCoins());
            assertTrue(tmp.isActive());
        } catch (NotEnoughCoinsException e) {
            assert false;
        }
        tmp = new SpecialCharacter(selectedSpecialCharacterID, students);
        try {
            this.players.get(2).paySpecialCharacter(tmp);
        } catch (NotEnoughCoinsException e) {
            assert true;
        }
    }

    /**
     * Tests whether returns the school board of the player.
     */
    @Test
    void getSchoolBoard() {
        SchoolBoard tmp;

        tmp = this.players.get(0).getSchoolBoard();
        assertEquals(TowerType.BLACK, tmp.getTowerType());
        assertEquals(6, tmp.getTowersNumber());

        tmp = this.players.get(1).getSchoolBoard();
        assertEquals(TowerType.WHITE, tmp.getTowerType());
        assertEquals(6, tmp.getTowersNumber());

        tmp = this.players.get(2).getSchoolBoard();
        assertEquals(TowerType.GREY, tmp.getTowerType());
        assertEquals(6, tmp.getTowersNumber());
    }

    /**
     * Tests whether 2 player are equals.
     */
    @Test
    void testEquals() {
        assertNotEquals(this.players.get(0), this.players.get(1));
        assertNotEquals(this.players.get(1), this.players.get(2));
        assertNotEquals(this.players.get(2), this.players.get(1));

        this.players.add(new Player("Matteo", WizardType.YELLOW, 6, TowerType.BLACK, this.entrance));

        assertEquals(this.players.get(0), this.players.get(3));
    }
}