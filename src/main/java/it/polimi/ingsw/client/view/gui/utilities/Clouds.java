package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.model.Cloud;
import it.polimi.ingsw.client.view.gui.CommandAssembler;
import javafx.scene.Group;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Clouds {

    private Clouds() {
    }

    public static List<CloudContainer> get(List<Cloud> clouds, int playersNumber, CommandAssembler assembler) {

        CloudContainer cloudContainer;
        List<CloudContainer> list = new ArrayList<>();

        for (Cloud cloud : clouds) {
            final int cloudId = clouds.indexOf(cloud);
            cloudContainer = new CloudContainer();
            list.add(cloudContainer);

            Group group = new Group();
            cloudContainer.setPane(group);

            Button cloudButton = new Button("");
            cloudButton.setStyle("-fx-border-color: #FCFFAD;" +
                    "-fx-border-width: 1px;" +
                    "-fx-border-radius: 50em;" +
                    "-fx-background-radius: 50em;" +
                    "-fx-background-color: radial-gradient(focus-distance 0%, center 50% 50%, radius 90%, transparent, #FCFFAD);" +
                    "-fx-min-width: 130px;" +
                    "-fx-min-height: 130px;");
            cloudButton.setVisible(false);
            cloudButton.setOnAction(mouseEvent -> assembler.manageCloudSelection(cloudId));
            group.getChildren().addAll(Images.cloud(), Grids.cloud(cloud, playersNumber, cloudContainer), cloudButton);
        }

        return Collections.unmodifiableList(list);
    }
}
