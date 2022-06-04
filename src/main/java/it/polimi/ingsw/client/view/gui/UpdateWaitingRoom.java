package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.utilities.ClientStates;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateWaitingRoom extends Thread {

    private final ClientGui client;
    private final Object lock;

    public UpdateWaitingRoom(ClientGui client) {
        this.client = client;
        lock = new Object();
    }

    @Override
    public void run() {
        List<Label> players = new ArrayList<>();
        Map<String, Boolean> waitingRoom;
        int index;

        while (client.getController().getClientState().equals(ClientStates.GAME_WAITING_ROOM)) {

            synchronized (lock) {

                players.clear();

                waitingRoom = client.getController().getGameModel().getWaitingRoom();
                index = (int) waitingRoom.entrySet().stream().filter(Map.Entry::getValue).count();

                for (Map.Entry<String, Boolean> entry : waitingRoom.entrySet())
                    if (Boolean.TRUE.equals(entry.getValue())) {
                        Label label = new Label(entry.getKey());
                        if (index != 1)
                            label.setOpaqueInsets(new Insets(0, 0, 10, 0));
                        if (Boolean.TRUE.equals(entry.getValue()))
                            label.setTextFill(Color.rgb(255, 255, 255));
                        index--;
                        players.add(label);
                    }

                WaitingRoom.update(players);

                try {
                    lock.wait(1000);
                } catch (InterruptedException e) {
                    client.getController().resetGame();
                }
            }
        }

        WaitingRoom.changeScene(ClientStates.GAME_RUNNING);
    }
}
