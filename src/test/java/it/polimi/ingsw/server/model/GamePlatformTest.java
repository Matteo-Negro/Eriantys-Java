package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.Island;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.WizardType;
import it.polimi.ingsw.utilities.exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test of class GamePlatform.
 *
 * @author Matteo Negro
 */
class GamePlatformTest {
    private List<GamePlatform> gamePlatforms;

    /**
     * Generates a new GamePlatform.
     */
    @BeforeEach
    void setUp() {
        this.gamePlatforms = new ArrayList<>();

        this.gamePlatforms.add(new GamePlatform(2, false));
        try {
            this.gamePlatforms.get(0).addPlayer("Matteo");
            this.gamePlatforms.get(0).addPlayer("Matteo");
        } catch (AlreadyExistingPlayerException | FullGameException e) {
            assert true;
        }
        try {
            this.gamePlatforms.get(0).addPlayer("Motta");
            this.gamePlatforms.get(0).addPlayer("Matteo");
        } catch (AlreadyExistingPlayerException | FullGameException e) {
            assert true;
        }

        List<Player> tmpPlayer = new ArrayList<>();
        Map<HouseColor, Integer> entrance = new EnumMap<>(HouseColor.class);

        Arrays.stream(HouseColor.values()).forEach(color -> entrance.put(color, !color.equals(HouseColor.YELLOW) ? 2 : 1));
        tmpPlayer.add(new Player("Matteo", WizardType.YELLOW, 6, TowerType.BLACK, entrance));
        tmpPlayer.add(new Player("Motta", WizardType.WHITE, 6, TowerType.WHITE, entrance));
        tmpPlayer.add(new Player("Milici", WizardType.GREEN, 6, TowerType.GREY, entrance));
        this.gamePlatforms.add(new GamePlatform(true, new GameBoard(3, true), tmpPlayer, tmpPlayer, tmpPlayer.get(0).getName()));
        try {
            this.gamePlatforms.get(1).addPlayer("Matteo");
            this.gamePlatforms.get(1).addPlayer("Motta");
            this.gamePlatforms.get(1).addPlayer("Milici");
        } catch (AlreadyExistingPlayerException | FullGameException e) {
            assert true;
        }
    }

    /**
     * Allows the garbage collector to delete the created GamePlatform.
     */
    @AfterEach
    void tearDown() {
        this.gamePlatforms = null;
    }

    /**
     * Tests whether returns the list of players.
     */
    @Test
    void getPlayers() {
        List<Player> tmp;

        tmp = this.gamePlatforms.get(0).getPlayers();
        assertEquals(2, tmp.size());
        tmp.forEach(p -> {
            assert p.getName().equals("Matteo") || p.getName().equals("Motta") || p.getAssistants().size() == 10;
        });

        tmp = this.gamePlatforms.get(1).getPlayers();
        assertEquals(3, tmp.size());
        tmp.forEach(p -> {
            assert p.getName().equals("Matteo") || p.getName().equals("Motta") || p.getName().equals("Milici") || p.getAssistants().size() == 10;
        });
    }

    /**
     * Tests whether returns the list of players sorted by the right game order.
     */
    @Test
    void getTurnOrder() {
        List<Player> tmp;

        tmp = this.gamePlatforms.get(0).getTurnOrder();
        assertEquals("Matteo", tmp.get(0).getName());
        assertEquals("Motta", tmp.get(1).getName());

        tmp = this.gamePlatforms.get(1).getTurnOrder();
        assertEquals("Matteo", tmp.get(0).getName());
        assertEquals("Motta", tmp.get(1).getName());
        assertEquals("Milici", tmp.get(2).getName());
    }

    /**
     * Tests whether returns the right player given him/her name.
     */
    @Test
    void getPlayerByName() {
        assertEquals("Matteo", this.gamePlatforms.get(0).getPlayerByName("Matteo").getName());
        assertEquals("Motta", this.gamePlatforms.get(0).getPlayerByName("Motta").getName());

        assertEquals("Matteo", this.gamePlatforms.get(1).getPlayerByName("Matteo").getName());
        assertEquals("Motta", this.gamePlatforms.get(1).getPlayerByName("Motta").getName());
        assertEquals("Milici", this.gamePlatforms.get(1).getPlayerByName("Milici").getName());
    }

    /**
     * Tests whether returns the game is expert.
     */
    @Test
    void isExpert() {
        assertFalse(this.gamePlatforms.get(0).isExpert());

        assertTrue(this.gamePlatforms.get(1).isExpert());
    }

    /**
     * Tests whether returns the current player.
     */
    @Test
    void getCurrentPlayer() {
        assertEquals("Matteo", this.gamePlatforms.get(0).getCurrentPlayer().getName());
        try {
            this.gamePlatforms.get(0).nextTurn();
        } catch (RoundConcluded e) {
            assert false;
        }
        assertEquals("Motta", this.gamePlatforms.get(0).getCurrentPlayer().getName());

        assertEquals("Matteo", this.gamePlatforms.get(1).getCurrentPlayer().getName());
        try {
            this.gamePlatforms.get(1).nextTurn();
        } catch (RoundConcluded e) {
            assert false;
        }
        assertEquals("Motta", this.gamePlatforms.get(1).getCurrentPlayer().getName());
    }

    /**
     * Tests whether returns game board.
     */
    @Test
    void getGameBoard() {
        try {
            this.gamePlatforms.get(0).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(0).getPlayerByName("Matteo"),
                    this.gamePlatforms.get(0).getPlayerByName("Matteo").playAssistant(6)
            );
            this.gamePlatforms.get(0).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(0).getPlayerByName("Motta"),
                    this.gamePlatforms.get(0).getPlayerByName("Motta").playAssistant(2)
            );
        } catch (AlreadyPlayedException | IllegalMoveException e) {
            assert false;
        }

        assertEquals(2, this.gamePlatforms.get(0).getGameBoard().getClouds().size());
        assertEquals(2, this.gamePlatforms.get(0).getGameBoard().getPlayedAssistants().size());
        assertEquals(12, this.gamePlatforms.get(0).getGameBoard().getIslands().size());
        for (Island i : this.gamePlatforms.get(0).getGameBoard().getIslands())
            if (i.getId() != 0 && i.getId() != 6)
                assertEquals(1, i.getStudents().values().stream().mapToInt(j -> j).sum());
        assertFalse(this.gamePlatforms.get(0).getGameBoard().getBag().isEmpty());

        try {
            this.gamePlatforms.get(1).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(1).getPlayerByName("Matteo"),
                    this.gamePlatforms.get(1).getPlayerByName("Matteo").playAssistant(6)
            );
            this.gamePlatforms.get(1).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(1).getPlayerByName("Motta"),
                    this.gamePlatforms.get(1).getPlayerByName("Motta").playAssistant(2)
            );
            this.gamePlatforms.get(1).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(1).getPlayerByName("Milici"),
                    this.gamePlatforms.get(1).getPlayerByName("Milici").playAssistant(1)
            );
        } catch (AlreadyPlayedException | IllegalMoveException e) {
            assert false;
        }
        assertEquals(3, this.gamePlatforms.get(1).getGameBoard().getClouds().size());
        assertEquals(3, this.gamePlatforms.get(1).getGameBoard().getPlayedAssistants().size());
        assertEquals(12, this.gamePlatforms.get(1).getGameBoard().getIslands().size());
        for (Island i : this.gamePlatforms.get(0).getGameBoard().getIslands())
            if (i.getId() != 0 && i.getId() != 6)
                assertEquals(1, i.getStudents().values().stream().mapToInt(j -> j).sum());
        assertFalse(this.gamePlatforms.get(1).getGameBoard().getBag().isEmpty());
        assertEquals(3, this.gamePlatforms.get(1).getGameBoard().getCharacters().size());
    }

    /**
     * Tests whether the round incrementation and co work.
     */
    @Test
    void nextRound() {
        try {
            this.gamePlatforms.get(0).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(0).getPlayerByName("Matteo"),
                    this.gamePlatforms.get(0).getPlayerByName("Matteo").playAssistant(6)
            );
            this.gamePlatforms.get(0).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(0).getPlayerByName("Motta"),
                    this.gamePlatforms.get(0).getPlayerByName("Motta").playAssistant(2)
            );
        } catch (AlreadyPlayedException | IllegalMoveException e) {
            assert false;
        }
        this.gamePlatforms.get(0).updateTurnOrder();
        this.gamePlatforms.get(0).nextRound();
        assertTrue(this.gamePlatforms.get(0).getGameBoard().getPlayedAssistants().isEmpty());
        assertEquals("Motta", this.gamePlatforms.get(0).getCurrentPlayer().getName());
        try {
            this.gamePlatforms.get(0).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(0).getPlayerByName("Matteo"),
                    this.gamePlatforms.get(0).getPlayerByName("Matteo").playAssistant(2)
            );
            this.gamePlatforms.get(0).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(0).getPlayerByName("Motta"),
                    this.gamePlatforms.get(0).getPlayerByName("Motta").playAssistant(6)
            );
        } catch (AlreadyPlayedException | IllegalMoveException e) {
            assert false;
        }
        this.gamePlatforms.get(0).updateTurnOrder();
        assertEquals("Matteo", this.gamePlatforms.get(0).getTurnOrder().get(0).getName());

        try {
            this.gamePlatforms.get(1).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(1).getPlayerByName("Matteo"),
                    this.gamePlatforms.get(1).getPlayerByName("Matteo").playAssistant(6)
            );
            this.gamePlatforms.get(1).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(1).getPlayerByName("Motta"),
                    this.gamePlatforms.get(1).getPlayerByName("Motta").playAssistant(2)
            );
            this.gamePlatforms.get(1).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(1).getPlayerByName("Milici"),
                    this.gamePlatforms.get(1).getPlayerByName("Milici").playAssistant(1)
            );
        } catch (AlreadyPlayedException | IllegalMoveException e) {
            assert false;
        }
        this.gamePlatforms.get(1).updateTurnOrder();
        this.gamePlatforms.get(1).nextRound();
        assertTrue(this.gamePlatforms.get(1).getGameBoard().getPlayedAssistants().isEmpty());
        assertEquals("Milici", this.gamePlatforms.get(1).getCurrentPlayer().getName());
        try {
            this.gamePlatforms.get(1).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(1).getPlayerByName("Matteo"),
                    this.gamePlatforms.get(1).getPlayerByName("Matteo").playAssistant(1)
            );
            this.gamePlatforms.get(1).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(1).getPlayerByName("Motta"),
                    this.gamePlatforms.get(1).getPlayerByName("Motta").playAssistant(6)
            );
            this.gamePlatforms.get(1).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(1).getPlayerByName("Milici"),
                    this.gamePlatforms.get(1).getPlayerByName("Milici").playAssistant(2)
            );
        } catch (AlreadyPlayedException | IllegalMoveException e) {
            assert false;
        }
        this.gamePlatforms.get(0).updateTurnOrder();
        assertEquals("Matteo", this.gamePlatforms.get(0).getTurnOrder().get(0).getName());
    }

    /**
     * Tests whether the update of the current player, after the planning phase, works.
     */
    @Test
    void updateTurnOrder() {
        try {
            this.gamePlatforms.get(0).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(0).getPlayerByName("Matteo"),
                    this.gamePlatforms.get(0).getPlayerByName("Matteo").playAssistant(6)
            );
            this.gamePlatforms.get(0).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(0).getPlayerByName("Motta"),
                    this.gamePlatforms.get(0).getPlayerByName("Motta").playAssistant(2)
            );
        } catch (AlreadyPlayedException | IllegalMoveException e) {
            assert false;
        }
        this.gamePlatforms.get(0).updateTurnOrder();
        assertEquals("Motta", this.gamePlatforms.get(0).getTurnOrder().get(0).getName());

        try {
            this.gamePlatforms.get(1).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(1).getPlayerByName("Matteo"),
                    this.gamePlatforms.get(1).getPlayerByName("Matteo").playAssistant(1)
            );
            this.gamePlatforms.get(1).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(1).getPlayerByName("Motta"),
                    this.gamePlatforms.get(1).getPlayerByName("Motta").playAssistant(6)
            );
            this.gamePlatforms.get(1).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(1).getPlayerByName("Milici"),
                    this.gamePlatforms.get(1).getPlayerByName("Milici").playAssistant(2)
            );
        } catch (AlreadyPlayedException | IllegalMoveException e) {
            assert false;
        }
        this.gamePlatforms.get(1).updateTurnOrder();
        assertEquals("Matteo", this.gamePlatforms.get(1).getTurnOrder().get(0).getName());
    }

    /**
     * Tests whether the turn incrementation and co work.
     */
    @Test
    void nextTurn() {
        try {
            this.gamePlatforms.get(0).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(0).getPlayerByName("Matteo"),
                    this.gamePlatforms.get(0).getPlayerByName("Matteo").playAssistant(6)
            );
            this.gamePlatforms.get(0).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(0).getPlayerByName("Motta"),
                    this.gamePlatforms.get(0).getPlayerByName("Motta").playAssistant(2)
            );
        } catch (AlreadyPlayedException | IllegalMoveException e) {
            assert false;
        }
        this.gamePlatforms.get(0).updateTurnOrder();
        assertEquals("Motta", this.gamePlatforms.get(0).getCurrentPlayer().getName());
        try {
            this.gamePlatforms.get(0).nextTurn();
        } catch (RoundConcluded e) {
            assert false;
        }
        assertEquals("Matteo", this.gamePlatforms.get(0).getCurrentPlayer().getName());
        try {
            this.gamePlatforms.get(0).nextTurn();
        } catch (RoundConcluded e) {
            assert true;
        }

        try {
            this.gamePlatforms.get(1).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(1).getPlayerByName("Matteo"),
                    this.gamePlatforms.get(1).getPlayerByName("Matteo").playAssistant(1)
            );
            this.gamePlatforms.get(1).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(1).getPlayerByName("Motta"),
                    this.gamePlatforms.get(1).getPlayerByName("Motta").playAssistant(6)
            );
            this.gamePlatforms.get(1).getGameBoard().addPlayedAssistant(
                    this.gamePlatforms.get(1).getPlayerByName("Milici"),
                    this.gamePlatforms.get(1).getPlayerByName("Milici").playAssistant(2)
            );
        } catch (AlreadyPlayedException | IllegalMoveException e) {
            assert false;
        }
        this.gamePlatforms.get(1).updateTurnOrder();
        assertEquals("Matteo", this.gamePlatforms.get(1).getCurrentPlayer().getName());
        try {
            this.gamePlatforms.get(1).nextTurn();
        } catch (RoundConcluded e) {
            assert false;
        }
        assertEquals("Milici", this.gamePlatforms.get(1).getCurrentPlayer().getName());
        try {
            this.gamePlatforms.get(1).nextTurn();
        } catch (RoundConcluded e) {
            assert false;
        }
        assertEquals("Motta", this.gamePlatforms.get(1).getCurrentPlayer().getName());
        try {
            this.gamePlatforms.get(1).nextTurn();
        } catch (RoundConcluded e) {
            assert true;
        }
    }
}