package it.polimi.ingsw.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * The entity containing the tcp connection socket of the client connected to the game server and its input and output streams.
 *
 * @author Riccardo Milici
 */
public class User {

    private final Socket socket;
    private String username;
    private boolean connected;
    private final ObjectInputStream socketInputStream;
    private final ObjectOutputStream socketOutputStream;

    /**
     * Class constructor.
     * Creates an instance of the class, containing the username, tcp socket and its input and output streams.
     *
     * @param socket The user's tcp socket, used to communicate with the game server throw the network.
     * @throws IOException Thrown if an error occurs during the input and output streams' opening.
     */
    public User(Socket socket) throws IOException {
        this.socket = socket;
        this.username = null;
        this.connected = true;

        socketInputStream = new ObjectInputStream(socket.getInputStream());
        socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
        socket.setSoTimeout(10 * 1000);
    }

    /**
     * Associates a name to the user.
     *
     * @param username The name chosen.
     */
    public void putName(String username) {
        this.username = username;
    }

    /**
     * Sets the connected attribute to true or false, according to the user socket's current connection status.
     *
     * @param connectionStatus Current user socket's connection status.
     */
    public void setConnected(boolean connectionStatus) {
        this.connected = connectionStatus;
    }

    /**
     * Returns the user's tcp connection socket.
     *
     * @return socket attribute.
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Returns the name associated to the user.
     *
     * @return username attribute.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the current socket's connection status (true->connected | false->disconnected).
     *
     * @return connected attribute.
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Reads the incoming message from the tcp socket.
     *
     * @return A JsonObject containing the command received.
     * @throws IOException            Thrown if an error occurs during the socket input stream read.
     * @throws ClassNotFoundException Thrown if an error occurs during the socket input stream read.
     */
    public synchronized JsonObject getCommand() throws IOException, ClassNotFoundException {

        return JsonParser.parseString((String) socketInputStream.readObject()).getAsJsonObject();
    }

    /**
     * Sends a message to the user client through the tcp socket.
     *
     * @param command A JsonObject containing the command to send.
     * @throws IOException Thrown if an error occurs during the socket input stream write.
     */
    public synchronized void sendCommand(JsonObject command) throws IOException {

        socketOutputStream.writeObject(command.getAsString());
    }

}
