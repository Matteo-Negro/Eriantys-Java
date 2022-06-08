package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.utilities.TowerType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Images {

    private static List<Image> clouds = null;
    private static List<Image> islands = null;
    private static Image motherNature = null;
    private static Map<TowerType, Image> towers = null;

    private Images() {
    }

    static ImageView island() {

        if (islands == null)
            initializeIslands();

        ImageView imageView = new ImageView();
        imageView.setFitWidth(162);
        imageView.setFitHeight(156);

        imageView.setImage(islands.get(ThreadLocalRandom.current().nextInt(islands.size())));

        return imageView;
    }

    static ImageView motherNature(boolean visible) {

        if (motherNature == null)
            motherNature = new Image("/pawns/mother_nature.png");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(30);
        imageView.setFitHeight(43);

        imageView.setImage(motherNature);
        imageView.setVisible(visible);

        return imageView;
    }

    static ImageView tower(TowerType tower) {

        if (towers == null)
            initializeTowers();

        ImageView imageView = new ImageView();
        imageView.setFitWidth(32);
        imageView.setFitHeight(52);

        if (tower != null)
            imageView.setImage(towers.get(tower));
        imageView.setVisible(tower != null);

        return imageView;
    }

    static Image getCloud(int id) {
        if (clouds == null)
            initializeClouds();
        return clouds.get(id % clouds.size());
    }

    static Image getIsland(int id) {
        if (islands == null)
            initializeIslands();
        return islands.get(id % islands.size());
    }

    static Image getTower(TowerType towerType) {
        if (towers == null)
            initializeTowers();
        return towers.get(towerType);
    }

    private static void initializeClouds() {
        List<Image> images = new ArrayList<>();
        for (int index = 1; index <= 4; index++)
            images.add(new Image(String.format("/realm/clouds/%d.png", index)));
        clouds = Collections.unmodifiableList(images);
    }

    private static void initializeIslands() {
        List<Image> images = new ArrayList<>();
        for (int index = 1; index <= 3; index++)
            images.add(new Image(String.format("/realm/islands/%d.png", index)));
        islands = Collections.unmodifiableList(images);
    }

    private static void initializeTowers() {
        Map<TowerType, Image> images = new EnumMap<>(TowerType.class);
        for (TowerType tower : TowerType.values())
            images.put(tower, new Image(String.format("/pawns/towers/realm/%s.png", tower.name().toLowerCase(Locale.ROOT))));
        towers = Collections.unmodifiableMap(images);
    }
}
