package it.polimi.ingsw.network.client;

import it.polimi.ingsw.view.cli.Realm;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

import static it.polimi.ingsw.view.cli.Utilities.clearScreen;
import static org.fusesource.jansi.Ansi.ansi;

public class Client {

    /**
     * Graphics types: CLI or GUI.
     */
    public enum GraphicsType {CLI, GUI}

    /**
     * Default constructor.
     *
     * @param graphicsType The type of interface that has to be instanced.
     */
    public Client(GraphicsType graphicsType) throws IOException {
        if (graphicsType.equals(GraphicsType.CLI)) {
            Terminal terminal = TerminalBuilder.terminal();
            clearScreen(terminal);
            Realm.print(terminal);
            terminal.writer().print(ansi().cursorDown(terminal.getHeight()));
            terminal.flush();
        }
    }
}
