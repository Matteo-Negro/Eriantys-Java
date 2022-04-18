package it.polimi.ingsw.controller;

import com.google.gson.JsonObject;
import it.polimi.ingsw.ServerLauncher;

import java.io.IOException;
import java.net.Socket;

public class Matchmaking extends Thread{

    private User user;
    private ServerLauncher gameServer;

    public Matchmaking(Socket userSocket, ServerLauncher gameServer){

        this.user = null;
        this.gameServer = gameServer;

        try {
            this.user = new User(userSocket);

        }catch(IOException i){
            try{userSocket.close();}
            catch(IOException c){
                //exception handle
            }
            //exception handle
        }
    }

    public void run() {
        manageCommand();
    }

    private void manageCommand() {
        JsonObject command = null;

        do{
            try {
                command = user.getCommand();
            }catch(IOException ioe) {
                //send an error message to the client
                try{
                    user.getSocket().close();
                }catch(IOException ioe_ioe){
                    //exception handle
                }
                return;
            }
            catch(ClassNotFoundException cnfe) {
                //send an error message to the client
                try{
                    user.getSocket().close();
                }catch(IOException cnfe_ioe){
                    //exception handle
                }
                return;
            }

        }while(command == null);

        //command parsing
    }

//    private void createGame(playersNumber, expertMode) {
//        gameServer.addGame(playersNumber, expertMode);
//    }

//    private void searchGame(String gameId) {
//
//        GameController desiredGame = null;
//
//        try{
//            desiredGame = gameServer.findGame(gameId);
//            }catch(GameNotFoundException gnfe){
//                 //send an error message to the client
//                 return;
//            }
//
//         gameServer.joinGame(user, desiredGame);
//    }
}
