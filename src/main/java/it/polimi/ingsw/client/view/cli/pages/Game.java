package it.polimi.ingsw.client.view.cli.pages;

import it.polimi.ingsw.client.model.GameModel;
import it.polimi.ingsw.client.view.cli.pages.subparts.PlayerStuff;
import it.polimi.ingsw.client.view.cli.pages.subparts.Realm;
import it.polimi.ingsw.utilities.GameControllerState;
import it.polimi.ingsw.utilities.Phase;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * Game CLI printer.
 *
 * @author Matteo Negro
 */
public class Game {

    private Game() {
    }

    /**
     * Prints the whole game selection centering it.
     *
     * @param terminal          Terminal where to write.
     * @param gameModel         The model of the game where the data are stored.
     * @param gameId            The id of the game.
     * @param round             The number of the round.
     * @param currentPlayerTurn true if it's the turn of the current player.
     */
    public static void print(Terminal terminal, GameModel gameModel, String gameId, int round, boolean currentPlayerTurn) {
        terminal.writer().print(printBorder(terminal.getHeight(), terminal.getWidth(), gameId, round, currentPlayerTurn, gameModel.getPhase(), gameModel.getSubPhase()));
        terminal.flush();
        terminal.writer().print(ansi().cursor((terminal.getHeight() - 31 - 5) / 2, (terminal.getWidth() - 165 - 6) / 2));
        Realm.print(terminal, gameModel.getGameBoard());

        if (!gameModel.isExpert()) {
            if (gameModel.getPlayersNumber() == 2)
                terminal.writer().print(ansi().cursor((terminal.getHeight() - 15 - 6) / 2, (terminal.getWidth() - 165 - 6) / 2));
            else
                terminal.writer().print(ansi().cursor((terminal.getHeight() - 29 - 6) / 2, (terminal.getWidth() - 165 - 6) / 2));
        } else {
            switch (gameModel.getPlayersNumber()) {
                case 2 ->
                        terminal.writer().print(ansi().cursor((terminal.getHeight() - 26 - 6) / 2, (terminal.getWidth() - 165 - 6) / 2));
                case 3 ->
                        terminal.writer().print(ansi().cursor((terminal.getHeight() - 29 - 6) / 2, (terminal.getWidth() - 165 - 6) / 2));
                default ->
                        terminal.writer().print(ansi().cursor((terminal.getHeight() - 40 - 6) / 2, (terminal.getWidth() - 165 - 6) / 2));
            }
        }
        PlayerStuff.print(terminal, gameModel.getPlayers(), gameModel.isExpert(), gameModel.getGameBoard().getSpecialCharacters());
        terminal.writer().print(ansi().cursor(terminal.getHeight() - 4, 1));
        terminal.flush();
    }

    /**
     * Prints the border of the page.
     *
     * @param y                 The y-size of the terminal.
     * @param x                 The x-size of the terminal.
     * @param gameId            The id of the game.
     * @param currentPlayerTurn true if it's the turn of the current player.
     * @param phase             The current phase of the game.
     * @param subphase          The current subphase of the game.
     * @return The generated Ansi stream.
     */
    private static Ansi printBorder(int y, int x, String gameId, int round, boolean currentPlayerTurn, Phase phase, GameControllerState subphase) {
        Ansi ansi = new Ansi();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();

        ansi.cursor(2, 2);
        String stats = String.format("┌──[ ID: %s ]──[ Round: %d ]──[ Phase: %s ]", gameId, round, subphase.name().toLowerCase(Locale.ROOT));
        for (int c = 0; c < x - 23 - stats.length(); c++) {
            if (c == 0)
                ansi.a(stats);
            else if (c == x - 24 - stats.length())
                ansi.a("[ " + dtf.format(now) + " ]──┐");
            else
                ansi.a("─");
        }

        for (int r = 3; r < y - 5; r++) {
            ansi.cursor(r, 2);
            ansi.a("│");
        }

        for (int r = 3; r < y - 5; r++) {
            ansi.cursor(r, x - 1);
            ansi.a("│");
        }

        String str;
        if (currentPlayerTurn) {
            if (phase.equals(Phase.PLANNING))
                str = "└──[ Play an assistant ]";
            else
                str = "└──[ It's your turn ]";
        } else
            str = "└";

        ansi.cursor(y - 5, 2);
        for (int c = 0; c < x - 1 - str.length(); c++) {
            if (c == 0) ansi.a(str);
            else if (c == x - 2 - str.length()) ansi.a("┘");
            else ansi.a("─");
        }

        ansi.cursor(2, 3);

        return ansi;
    }
}
