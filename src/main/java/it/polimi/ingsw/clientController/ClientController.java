package it.polimi.ingsw.clientController;

import it.polimi.ingsw.clientStatus.Status;
import it.polimi.ingsw.utilities.ClientStates;

import java.io.IOException;
import java.net.Socket;

public class ClientController {
    private String userName;
    private GameServer gameServer;
    private String serverIp;
    private int serverPort;
    private Status status;
    private ClientStates state;
    private String phase;
    private GameControllerStates subPhase;

    public ClientController(String serverIp, int serverPort){
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.state = ClientStates.MAIN_MENU;
        this.phase = null;
        this.subPhase = null;
    }

    public void run(){
        System.out.println("\n * Client is attempting to connect to the server.");

        try{
            Socket hostSocket = new Socket(serverIp, serverPort);
            this.gameServer = new GameServer(hostSocket, this);
            System.out.println("\n * Client has successfully connected to the server.");
        }catch(IOException ioe){
            this.setState(ClientStates.CONNECTION_LOST);
        }

        while(true){
            while(!gameServer.isConnected()){
                try{
                    gameServer.wait();
                }catch(InterruptedException ie){
                    this.setState(ClientStates.CONNECTION_LOST);
                    break;
                }
            }

            switch(getState()){
                //TODO manage different client's states.
            }
        }


    }

    private void setState(ClientStates newState){
        this.state = newState;
    }

    private ClientStates getState(){
        return this.state;
    }

}
