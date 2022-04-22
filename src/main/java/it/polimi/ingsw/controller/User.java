package it.polimi.ingsw.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * User class: containing the tcp connection socket of the client connected to the game server and its input and output streams.
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
     * @param socket
     * @throws IOException
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
     * @param username
     */
    public void putName(String username) {
        this.username = username;
    }

    /**
     * @param connectionStatus
     */
    public void setConnected(boolean connectionStatus) {
        this.connected = connectionStatus;
    }

    /**
     * @return
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public synchronized JsonObject getCommand() throws IOException, ClassNotFoundException {

        return JsonParser.parseString((String) socketInputStream.readObject()).getAsJsonObject();
    }

    /**
     * @param command
     * @throws IOException
     */
    public synchronized void sendCommand(JsonObject command) throws IOException {

        socketOutputStream.writeObject(command.getAsString());
    }

}
