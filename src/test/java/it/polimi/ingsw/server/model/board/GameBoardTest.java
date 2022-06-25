package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.player.Assistant;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.WizardType;
import it.polimi.ingsw.utilities.exceptions.IllegalMoveException;
import it.polimi.ingsw.utilities.exceptions.IslandNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test of class GameBoard.
 *
 * @author Riccardo Milici
 */
public class GameBoardTest {
    private final Bag bag = new Bag();
    private final List<Cloud> clouds = new ArrayList<>();
    private final List<Island> islands = new ArrayList<>();
    private final Map<HouseColor, Player> professors = new EnumMap<>(HouseColor.class);
    private final Map<Player, Assistant> playedAssistants = new HashMap<>();
    private final List<SpecialCharacter> characters = new ArrayList<>();
    private Player samplePlayer;
    private Island motherNatureIsland;

    private GameBoard gameBoard;

    /**
     * Generates a new GameBoard.
     */
    @BeforeEach
    void setup() {
        for (HouseColor color : HouseColor.values()) professors.put(color, null);
        for (int i = 0; i < 12; i++) {
            this.islands.add(new Island(null, i));
        }

        this.characters.add(new SpecialCharacter(2, null));
        this.characters.add(new SpecialCharacter(3, null));
        this.characters.add(new SpecialCharacter(4, null));

        Arrays.stream(HouseColor.values()).forEach(color -> this.professors.put(color, null));
        this.motherNatureIsland = this.islands.get(0);

        gameBoard = new GameBoard(this.bag.getStatus(), this.playedAssistants, this.islands, this.clouds, this.characters, this.professors, true, this.motherNatureIsland.getId());
    }

    /**
     * Allows the garbage collector to delete the created Assistant.
     */
    @AfterEach
    void tearDown() {
        this.gameBoard = null;
    }

    /**
     * Tests if the correct bag is returned.
     */
    @Test
    void getBag() {
        assertEquals(this.bag.getStatus(), gameBoard.getBag().getStatus());
    }

    /**
     * Tests if the correct islands are returned.
     */
    @Test
    void getClouds() {
        assertEquals(this.clouds, gameBoard.getClouds());
    }

    /**
     * Test if the correct islands are returned.
     */
    @Test
    void getIslands() {
        assertEquals(this.islands, gameBoard.getIslands());
    }

    /**
     * Test if the correct islands are returned.
     */
    @Test
    void getIslanById() {
        try {
            assertEquals(0, gameBoard.getIslandById(0).getId());
        } catch (IslandNotFoundException infe) {
            System.out.println("Island not found.");
        }
    }

    /**
     * Tests if the correct motherNatureIsland is returned;
     */
    @Test
    void getMotherNatureIsland() {
        assertEquals(this.motherNatureIsland, gameBoard.getMotherNatureIsland());
    }

    /**
     * Tests if the correct ignoreColor is returned;
     */
    @Test
    void getIgnoreColor() {
        assertNull(gameBoard.getIgnoreColor());
    }

    /**
     * Tests if the correct characters are returned;
     */
    @Test
    void getCharacters() {
        assertEquals(this.characters, gameBoard.getCharacters());
    }

    /**
     * Tests if the correct professors are returned;
     */
    @Test
    void getProfessors() {
        assertEquals(this.professors, gameBoard.getProfessors());
    }

    /**
     * Tests if the correct assistant is returned.
     */
    @Test
    void getAssistant() {
        Assistant sampleAssistant = new Assistant(1);
        Map<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) students.put(color, 0);
        this.samplePlayer = new Player("sample4", WizardType.YELLOW, 6, TowerType.WHITE, students);

        try {
            gameBoard.addPlayedAssistant(samplePlayer, sampleAssistant);
        } catch (IllegalMoveException ime) {
            System.out.println("Illegal move.");
        }

        assertEquals(sampleAssistant, gameBoard.getAssistant(samplePlayer));
    }

    /**
     * Tests if the correct assistants are returned.
     */
    @Test
    void getPlayedAssistants() {
        assertEquals(this.playedAssistants, gameBoard.getPlayedAssistants());
    }

    /**
     * Tests if the correct influenceBonus is returned.
     */
    @Test
    void getInfluenceBonus() {
        assertNull(gameBoard.getInfluenceBonus());
    }

    /**
     * Tests if the correct tieWinner is returned.
     */
    @Test
    void getTieWinner() {
        assertNull(gameBoard.getTieWinner());
    }

    /**
     * Tests if the correct influenceBonus is returned.
     */
    @Test
    void setInfluenceBonus() {
        Map<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) students.put(color, 0);
        this.samplePlayer = new Player("sample2", WizardType.FUCHSIA, 8, TowerType.BLACK, students);

        gameBoard.setInfluenceBonus(this.samplePlayer);

        assertEquals(this.samplePlayer, gameBoard.getInfluenceBonus());
    }

    /**
     * Tests if the correct tieWinner is returned.
     */
    @Test
    void setTieWinner() {
        Map<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) students.put(color, 0);
        this.samplePlayer = new Player("sample3", WizardType.GREEN, 5, TowerType.BLACK, students);

        gameBoard.setTieWinner(this.samplePlayer);

        assertEquals(this.samplePlayer, gameBoard.getTieWinner());
    }

    /**
     * Tests if the correct played assistant is returned.
     */
    @Test
    void addPlayedAssistant() {
        Assistant sampleAssistant = new Assistant(1);
        Map<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) students.put(color, 0);
        this.samplePlayer = new Player("sample4", WizardType.YELLOW, 6, TowerType.WHITE, students);
        Map<Player, Assistant> playedAssistants = new HashMap<>();
        playedAssistants.put(samplePlayer, sampleAssistant);

        try {
            gameBoard.addPlayedAssistant(samplePlayer, sampleAssistant);
        } catch (IllegalMoveException ime) {
            System.out.println("Illegal move.");
        }

        assertEquals(playedAssistants, gameBoard.getPlayedAssistants());
    }

    /**
     * Tests if the assistants list is flushed correctly.
     */
    @Test
    void flushAssistantsList() {
        gameBoard.flushAssistantsList();
        assertEquals(0, gameBoard.getPlayedAssistants().keySet().size());
    }

    /**
     * Tests if the professor is set correctly.
     */
    @Test
    void setProfessor() {
        Map<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) students.put(color, 0);
        this.samplePlayer = new Player("sample4", WizardType.YELLOW, 6, TowerType.WHITE, students);

        gameBoard.setProfessor(HouseColor.RED, samplePlayer);

        assertEquals(samplePlayer, gameBoard.getProfessors().get(HouseColor.RED));
    }

    /**
     * Tests if the tower is set correctly.
     */
    @Test
    void setTowerOnIsland() {
        try {
            gameBoard.setTowerOnIsland(gameBoard.getIslandById(0), TowerType.WHITE);
            assertEquals(TowerType.WHITE, gameBoard.getIslandById(0).getTower());
        } catch (IslandNotFoundException infe) {
            System.out.println("Island not found.");
        }
    }

    /**
     * Tests if the islands merge correctly.
     */
    @Test
    void mergeIslands() {
        try {
            gameBoard.setTowerOnIsland(gameBoard.getIslandById(0), TowerType.WHITE);
            gameBoard.setTowerOnIsland(gameBoard.getIslandById(1), TowerType.WHITE);
            assertEquals(2, gameBoard.getIslandById(0).getSize());
        } catch (IslandNotFoundException infe) {
            System.out.println("Island not found.");
        }
    }

    /**
     * Tests if the influence is calculated correctly.
     */
    @Test
    void getInfluence() {
        Map<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) students.put(color, 0);
        students.replace(HouseColor.BLUE, 4);
        this.samplePlayer = new Player("sample4", WizardType.YELLOW, 6, TowerType.WHITE, students);

        try {
            gameBoard.setProfessor(HouseColor.BLUE, samplePlayer);
            gameBoard.setTowerOnIsland(gameBoard.getIslandById(0), TowerType.WHITE);
            gameBoard.getIslandById(0).addStudent(HouseColor.BLUE);
            assertEquals(2, gameBoard.getInfluence(gameBoard.getIslandById(0)).get(samplePlayer));
        } catch (IslandNotFoundException infe) {
            System.out.println("Island not found.");
        }
    }

    /**
     * Tests if the effects are removed correctly.
     */
    @Test
    void removeEffects() {
        gameBoard.removeEffects();

        assertNull(gameBoard.getIgnoreColor());
        assertNull(gameBoard.getInfluenceBonus());
        assertNull(gameBoard.getTieWinner());
        for (SpecialCharacter sc : gameBoard.getCharacters()) {
            assertFalse(sc.isActive());
        }
    }

    /**
     * Tests if the ignoreColor is set correctly.
     */
    @Test
    void setIgnoreColor() {
        gameBoard.setIgnoreColor(HouseColor.BLUE);

        assertEquals(HouseColor.BLUE, gameBoard.getIgnoreColor());
    }

    /**
     * Tests if mother nature is oved correctly.
     */
    @Test
    void moveMotherNature() {
        Assistant sampleAssistant = new Assistant(4);
        try {
            gameBoard.moveMotherNature(gameBoard.getIslandById(1), sampleAssistant);
            assertEquals(gameBoard.getMotherNatureIsland(), gameBoard.getIslandById(1));
        } catch (IslandNotFoundException infe) {
            System.out.println("Island not found.");
        } catch (IllegalMoveException ime) {
            System.out.println("Illegal move.");
        }
    }

    /**
     * Tests if two game boards of the same players number and difficulty are different.
     */
    @Test
    void gameBoardsEquals() {
        GameBoard sampleGameBoard = new GameBoard(4, true);
        this.gameBoard = new GameBoard(4, true);

        assertNotEquals(sampleGameBoard, gameBoard);
    }

    /**
     * Tests if two game boards of the same players number and difficulty have different hash codes.
     */
    @Test
    void gameBoardsHash() {
        GameBoard sampleGameBoard = new GameBoard(4, true);
        this.gameBoard = new GameBoard(4, true);

        assertNotEquals(sampleGameBoard.hashCode(), gameBoard.hashCode());
    }
}
