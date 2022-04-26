package it.polimi.ingsw.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utilities.MessageCreator;
import it.polimi.ingsw.utilities.exceptions.FullGameException;
import it.polimi.ingsw.utilities.exceptions.GameNotFoundException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * The entity containing the tcp connection socket of the client connected to the game server and its input and output streams.
 *
 * @author Riccardo Milici
 * @author Riccardo Motta
 */
public class User extends Thread {

    private boolean connected;
    private final Object connectedLock;
    private final Scanner inputStream;
    private final PrintWriter outputStream;
    private final Server server;
    private final Ping ping;
    private GameController gameController;
    private boolean logged;

    /**
     * Class constructor.
     * Creates an instance of the class, containing the username, tcp socket and its input and output streams.
     *
     * @param socket The user's tcp socket, used to communicate with the game server throw the network.
     * @param server The main server instance.
     * @throws IOException Thrown if an error occurs during the input and output streams' opening.
     */
    public User(Socket socket, Server server) throws IOException {
        this.connected = true;
        this.server = server;
        this.connectedLock = new Object();
        this.ping = new Ping(this);
        this.gameController = null;
        this.logged = false;

        inputStream = new Scanner(socket.getInputStream());
        outputStream = new PrintWriter(socket.getOutputStream());
        socket.setSoTimeout(10000);
    }

    public boolean isLogged() {
        return logged;
    }

    public void run() {

        JsonObject incomingMessage;

        new Thread(ping).start();

        while (true) {

            synchronized (connectedLock) {
                if (!connected)
                    break;
            }

            try {
                incomingMessage = getCommand();
            } catch (IOException e) {
                // If socket time out expires.
                disconnected();
                break;
            }
            manageCommand(incomingMessage);
        }

        ping.interrupt();
    }

    /**
     * Reads the incoming message from the tcp socket.
     *
     * @return A JsonObject containing the command received.
     * @throws IOException Thrown if an error occurs during the socket input stream read.
     */
    private synchronized JsonObject getCommand() throws IOException {
        return JsonParser.parseString(inputStream.nextLine()).getAsJsonObject();
    }

    /**
     * Sends a message to the user client through the tcp socket.
     *
     * @param command A JsonObject containing the command to send.
     */
    public void sendMessage(JsonObject command) {
        synchronized (outputStream) {
            outputStream.println(command.toString());
            outputStream.flush();
        }
    }

    /**
     * Manages the user's command parsing and calls the "createGame(int playersNumber, boolean expertMode)" method or "searchGame(String gameCode)" method if requested.
     *
     * @param command The command to manage.
     */
    private void manageCommand(JsonObject command) {

        switch (command.get("type").getAsString()) {
            case "pong" -> {
            }
            case "gameCreation" -> sendMessage(MessageCreator.gameCreation(Matchmaking.gameCreation(command, server)));
            case "enterGame" -> {
                try {
                    gameController = Matchmaking.enterGame(command.get("code").getAsString(), server);
                } catch (FullGameException | GameNotFoundException e) {
                    gameController = null;
                }
                sendMessage(MessageCreator.enterGame(gameController));
            }
            case "login" -> {
                logged = Matchmaking.login(gameController, command.get("name").getAsString(), this);
                sendMessage(MessageCreator.login(logged));
            }
            case "logout" -> removeFromGame();
            default -> sendMessage(MessageCreator.error("Wrong command."));
        }
    }

    public void disconnected() {
        synchronized (connectedLock) {
            connected = false;
        }
        removeFromGame();
    }

    private void removeFromGame() {
        if (gameController == null)
            return;
        gameController.removeUser(this);
    }
}

// TODO: re-add username and match it to GameController's one
