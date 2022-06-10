package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Images {

    private static List<Image> clouds = null;
    private static List<Image> islands = null;
    private static Image motherNature = null;
    private static Map<HouseColor, Image> studentsRealm = null;
    private static Map<HouseColor, Image> studentsBoard = null;
    private static Map<TowerType, Image> towers = null;

    private Images() {
    }

    static ImageView cloud() {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(131);
        imageView.setFitHeight(128);
        imageView.setImage(getCloudById(ThreadLocalRandom.current().nextInt(4)));
        return imageView;
    }

    static ImageView island() {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(162);
        imageView.setFitHeight(156);
        imageView.setImage(getIslandById(ThreadLocalRandom.current().nextInt(3)));
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
        ImageView imageView = new ImageView();
        imageView.setFitWidth(32);
        imageView.setFitHeight(52);
        imageView.setImage(getTowerByColor(tower));
        imageView.setVisible(tower != null);
        return imageView;
    }

    static ImageView student2d(HouseColor houseColor) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        imageView.setImage(getStudent2dByColor(houseColor));
        return imageView;
    }

    static ImageView student3d(HouseColor houseColor) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        imageView.setImage(getStudent3dByColor(houseColor));
        return imageView;
    }

    static Image getCloudById(int id) {
        if (clouds == null)
            initializeClouds();
        return clouds.get(id % clouds.size());
    }

    static Image getIslandById(int id) {
        if (islands == null)
            initializeIslands();
        return islands.get(id % islands.size());
    }

    static Image getStudent3dByColor(HouseColor houseColor) {
        if (studentsRealm == null)
            initializeStudentsRealm();
        return studentsRealm.get(houseColor);
    }

    static Image getStudent2dByColor(HouseColor houseColor) {
        if (studentsBoard == null)
            initializeStudentsBoard();
        return studentsBoard.get(houseColor);
    }

    static Image getTowerByColor(TowerType towerType) {
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

    private static void initializeStudentsRealm() {
        Map<HouseColor, Image> images = new EnumMap<>(HouseColor.class);
        for (HouseColor houseColor : HouseColor.values())
            images.put(houseColor, new Image(String.format("/pawns/students/realm/%s.png", houseColor.name().toLowerCase(Locale.ROOT))));
        studentsRealm = Collections.unmodifiableMap(images);
    }

    private static void initializeStudentsBoard() {
        Map<HouseColor, Image> images = new EnumMap<>(HouseColor.class);
        for (HouseColor houseColor : HouseColor.values())
            images.put(houseColor, new Image(String.format("/pawns/students/board/%s.png", houseColor.name().toLowerCase(Locale.ROOT))));
        studentsBoard = Collections.unmodifiableMap(images);
    }

    private static void initializeTowers() {
        Map<TowerType, Image> images = new EnumMap<>(TowerType.class);
        for (TowerType tower : TowerType.values())
            images.put(tower, new Image(String.format("/pawns/towers/realm/%s.png", tower.name().toLowerCase(Locale.ROOT))));
        towers = Collections.unmodifiableMap(images);
    }
}
