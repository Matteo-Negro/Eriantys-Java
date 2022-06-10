package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.model.Cloud;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Clouds {

    private Clouds() {
    }

    public static List<CloudContainer> get(List<Cloud> clouds, int playersNumber) {

        CloudContainer cloudContainer;
        List<CloudContainer> list = new ArrayList<>();

        for (Cloud cloud : clouds) {

            cloudContainer = new CloudContainer();
            list.add(cloudContainer);

            Group group = new Group();
            cloudContainer.setPane(group);
            group.getChildren().addAll(Images.cloud(), Grids.cloud(cloud, playersNumber, cloudContainer));
        }

        return Collections.unmodifiableList(list);
    }
}
