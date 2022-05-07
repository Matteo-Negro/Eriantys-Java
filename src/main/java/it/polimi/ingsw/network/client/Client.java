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

    private final Terminal terminal;
    private int width;

    /**
     * Default constructor.
     *
     * @param graphicsType The type of the interface that has to be instanced.
     */
    public Client(GraphicsType graphicsType) throws IOException {
        terminal = TerminalBuilder.terminal();
        width = terminal.getWidth();
        clearScreen(terminal);
        Realm.print(terminal);
        terminal.writer().print(ansi().cursorDown(terminal.getHeight()));
        terminal.flush();
//        terminal.writer().print(ansi().reset().eraseScreen());
//        terminal.writer().print(ansi().reset().bgRed());
//        terminal.writer().println("Hello, World!");
//        terminal.getWidth();
//        terminal.writer().flush();
    }

//    private void test() {
//        terminal.writer().print(ansi().reset().eraseScreen());
//        //terminal.writer().print(PrintUtils.printIsland(terminal, 0, null, null, false, false, null));
//        Ansi ansi = new Ansi();
//        ansi.bg(Ansi.Color.BLACK);
//        ansi.fg(Ansi.Color.WHITE);
//        ansi.newline();
//        ansi.append("Hello, World! (1)");
//        ansi.newline();
//        ansi.append("Hello, World! (2)");
//        ansi.cursorUp(1);
//        ansi.cursorRight(3);
//        ansi.append("Test");
//        ansi.cursorDown(1);
//        terminal.writer().println(ansi);
//        terminal.flush();
//    }
}
