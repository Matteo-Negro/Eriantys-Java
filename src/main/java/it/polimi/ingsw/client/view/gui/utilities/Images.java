package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.utilities.EndType;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.WizardType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Static class for getting all the required images for the game.
 *
 * @author Riccardo Motta
 */
public class Images {

    private static final Object assistantLock = new Object();
    private static final Object banIconLock = new Object();
    private static final Object banIslandLock = new Object();
    private static final Object boardLock = new Object();
    private static final Object boardCoinsLock = new Object();
    private static final Object cloudsLock = new Object();
    private static final Object coinLock = new Object();
    private static final Object endTitlesLock = new Object();
    private static final Object islandsLock = new Object();
    private static final Object motherNatureLock = new Object();
    private static final Object professorsLock = new Object();
    private static final Object specialCharactersLock = new Object();
    private static final Object studentsRealmLock = new Object();
    private static final Object studentsBoardLock = new Object();
    private static final Object towersBoardLock = new Object();
    private static final Object towersRealmLock = new Object();
    private static final Object wizardsLock = new Object();
    private static List<Image> assistants = null;
    private static Image banIcon = null;
    private static Image banIsland = null;
    private static Image board = null;
    private static Image boardCoins = null;
    private static List<Image> clouds = null;
    private static Image coin = null;
    private static Map<EndType, Image> endTitles = null;
    private static List<Image> islands = null;
    private static Image motherNature = null;
    private static Map<HouseColor, Image> professors = null;
    private static List<Image> specialCharacters = null;
    private static Map<HouseColor, Image> studentsRealm = null;
    private static Map<HouseColor, Image> studentsBoard = null;
    private static Map<TowerType, Image> towersBoard = null;
    private static Map<TowerType, Image> towersRealm = null;
    private static Map<WizardType, Image> wizards = null;

    private Images() {
    }

    /**
     * Gets the desired assistant.
     *
     * @param id The id of the assistant.
     * @return The desired assistant.
     */
    static ImageView assistant(int id) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(135);
        imageView.setFitHeight(200);
        imageView.setImage(getAssistantById(id));
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 10, 0.5, 0.0, 1);");
        return imageView;
    }

    /**
     * Gets the ban icon.
     *
     * @return The ban icon.
     */
    static ImageView banIcon() {
        synchronized (banIconLock) {
            if (banIcon == null)
                banIcon = new Image("/ban/icon.png");
        }
        ImageView imageView = new ImageView();
        imageView.setFitWidth(42);
        imageView.setFitHeight(48);
        imageView.setImage(banIcon);
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 10, 0.5, 0.0, 2);");
        return imageView;
    }

    /**
     * Gets the ban for the island.
     *
     * @return The ban for the island.
     */
    static ImageView banIsland() {
        synchronized (banIslandLock) {
            if (banIsland == null)
                banIsland = new Image("/ban/island.png");
        }
        ImageView imageView = new ImageView();
        imageView.setFitWidth(162);
        imageView.setFitHeight(156);
        imageView.setImage(banIsland);
        imageView.setVisible(false);
        return imageView;
    }

    /**
     * Gets the board image.
     *
     * @return The board image.
     */
    static ImageView board() {
        synchronized (boardLock) {
            if (board == null)
                board = new Image("/realm/schoolboard.png");
        }
        ImageView imageView = new ImageView();
        imageView.setFitWidth(715);
        imageView.setFitHeight(305);
        imageView.setImage(board);
        return imageView;
    }

    /**
     * Gets the coins image for the board.
     *
     * @return The coins image for the board.
     */
    static ImageView boardCoins() {
        synchronized (boardCoinsLock) {
            if (boardCoins == null)
                boardCoins = new Image("/realm/schoolboard_coins.png");
        }
        ImageView imageView = new ImageView();
        imageView.setFitWidth(715);
        imageView.setFitHeight(305);
        imageView.setImage(board);
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 10, 0.5, 0.0, 2);");
        return imageView;
    }

    /**
     * Gets a random cloud image.
     *
     * @return A random cloud image.
     */
    static ImageView cloud() {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(131);
        imageView.setFitHeight(128);
        imageView.setImage(getCloudById(ThreadLocalRandom.current().nextInt(4)));
        return imageView;
    }

    /**
     * Gets the coin image.
     *
     * @return The coin image.
     */
    static ImageView coin() {
        synchronized (coinLock) {
            if (coin == null)
                coin = new Image("/coin.png");
        }
        ImageView imageView = new ImageView();
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setImage(coin);
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 10, 0.5, 0.0, 2);");
        return imageView;
    }

    /**
     * Gets a random cloud image.
     *
     * @return A random cloud image.
     */
    static ImageView island() {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(162);
        imageView.setFitHeight(156);
        imageView.setImage(getIslandById(ThreadLocalRandom.current().nextInt(3)));
        return imageView;
    }

    /**
     * Gets mother nature image.
     *
     * @return Mother nature image.
     */
    static ImageView motherNature() {
        synchronized (motherNatureLock) {
            if (motherNature == null)
                motherNature = new Image("/pawns/mother_nature.png");
        }
        ImageView imageView = new ImageView();
        imageView.setFitWidth(30);
        imageView.setFitHeight(43);
        imageView.setImage(motherNature);
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 10, 0.5, 0.0, 4);");
        return imageView;
    }

    /**
     * Gets the professor of the specified color.
     *
     * @param color The color of the professor.
     * @return The desired professor image.
     */
    static ImageView professor(HouseColor color) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(45);
        imageView.setFitHeight(39);
        imageView.setImage(getProfessorByColor(color));
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 10, 0.5, 0.0, 4);");
        return imageView;
    }

    /**
     * Gets the desired special character.
     *
     * @param id The id of the special character.
     * @return The desired special character.
     */
    static ImageView specialCharacter(int id) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(261);
        imageView.setFitHeight(384);
        imageView.setImage(getSpecialCharacterById(id));
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 10, 0.5, 0.0, 1);");
        return imageView;
    }

    /**
     * Gets the desired tower (board version).
     *
     * @param tower The tower's color.
     * @return The desired tower (board version).
     */
    static ImageView towerBoard(TowerType tower) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(37);
        imageView.setFitHeight(58);
        imageView.setImage(getTowerBoardByColor(tower));
        imageView.setVisible(tower != null);
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 10, 0.5, 0.0, 2);");
        return imageView;
    }

    /**
     * Gets the desired tower (realm version).
     *
     * @param tower The tower's color.
     * @return The desired tower (realm version).
     */
    static ImageView towerRealm(TowerType tower) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(32);
        imageView.setFitHeight(52);
        imageView.setImage(getTowerRealmByColor(tower));
        imageView.setVisible(tower != null);
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 10, 0.5, 0.0, 2);");
        return imageView;
    }

    /**
     * Gets the desired student (2D version).
     *
     * @param houseColor The student's color.
     * @return The desired student (2D version).
     */
    static ImageView student2d(HouseColor houseColor) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        imageView.setImage(getStudent2dByColor(houseColor));
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 10, 0.5, 0.0, 2);");
        return imageView;
    }

    /**
     * Gets the desired student (3D version).
     *
     * @param houseColor The student's color.
     * @return The desired student (3D version).
     */
    static ImageView student3d(HouseColor houseColor) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        imageView.setImage(getStudent3dByColor(houseColor));
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 10, 0.5, 0.0, 3);");
        return imageView;
    }

    /**
     * Gets the desired wizard.
     *
     * @param wizardType The color of the wizard.
     * @return The desired wizard.
     */
    static ImageView wizard(WizardType wizardType) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(135);
        imageView.setFitHeight(200);
        imageView.setImage(getWizardByType(wizardType));
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 10, 0.5, 0.0, 1);");
        return imageView;
    }

    /**
     * Gets the assistant according to the id.
     *
     * @param id The id of the assistant.
     * @return The desired assistant.
     */
    static Image getAssistantById(int id) {
        initializeAssistants();
        return assistants.get(id - 1);
    }

    /**
     * Gets the cloud according to the id.
     *
     * @param id The id of the cloud.
     * @return The desired cloud.
     */
    static Image getCloudById(int id) {
        initializeClouds();
        return clouds.get(id % clouds.size());
    }

    /**
     * Gets the island according to the id.
     *
     * @param id The id of the island.
     * @return The desired island.
     */
    static Image getIslandById(int id) {
        initializeIslands();
        return islands.get(id % islands.size());
    }

    /**
     * Gets the professor according to it's color.
     *
     * @param houseColor The color of the professor.
     * @return The desired professor.
     */
    static Image getProfessorByColor(HouseColor houseColor) {
        initializeProfessors();
        return professors.get(houseColor);
    }

    /**
     * Gets the student (2D version) according to it's color.
     *
     * @param houseColor The student's color.
     * @return The desired student (2D version).
     */
    static Image getStudent2dByColor(HouseColor houseColor) {
        initializeStudentsBoard();
        return studentsBoard.get(houseColor);
    }

    /**
     * Gets the student (3D version) according to it's color.
     *
     * @param houseColor The student's color.
     * @return The desired student (3D version).
     */
    static Image getStudent3dByColor(HouseColor houseColor) {
        initializeStudentsRealm();
        return studentsRealm.get(houseColor);
    }

    /**
     * Gets the required end title.
     *
     * @param endType The type of ending.
     * @return The required end title.
     */
    public static Image getEndTitleByState(EndType endType) {
        initializeEndTitles();
        return endTitles.get(endType);
    }

    /**
     * Gets the desired special character according to its id.
     *
     * @param id The id of the special character.
     * @return The desired special character.
     */
    static Image getSpecialCharacterById(int id) {
        initializeSpecialCharacters();
        return specialCharacters.get(id - 1);
    }

    /**
     * Gets the desired tower (board version) according to its color.
     *
     * @param towerType The color of the tower.
     * @return The desired tower (board version).
     */
    static Image getTowerBoardByColor(TowerType towerType) {
        initializeTowersBoard();
        return towersBoard.get(towerType);
    }

    /**
     * Gets the desired tower (realm version) according to its color.
     *
     * @param towerType The color of the tower.
     * @return The desired tower (realm version).
     */
    static Image getTowerRealmByColor(TowerType towerType) {
        initializeTowersRealm();
        return towersRealm.get(towerType);
    }

    /**
     * Gets the desired wizard according to its type.
     *
     * @param wizardType The type of the wizard.
     * @return The desired wizard.
     */
    static Image getWizardByType(WizardType wizardType) {
        initializeWizards();
        return wizards.get(wizardType);
    }

    /**
     * Initializes the assistants.
     */
    private static void initializeAssistants() {
        synchronized (assistantLock) {
            if (assistants != null)
                return;
            List<Image> images = new ArrayList<>();
            for (int index = 1; index <= 10; index++)
                images.add(new Image(String.format("/cards/assistants/%d.png", index)));
            assistants = Collections.unmodifiableList(images);
        }
    }

    /**
     * Initializes the clouds.
     */
    private static void initializeClouds() {
        synchronized (cloudsLock) {
            if (clouds != null)
                return;
            List<Image> images = new ArrayList<>();
            for (int index = 1; index <= 4; index++)
                images.add(new Image(String.format("/realm/clouds/%d.png", index)));
            clouds = Collections.unmodifiableList(images);
        }
    }

    /**
     * Initializes end titles.
     */
    private static void initializeEndTitles() {
        synchronized (endTitlesLock) {
            if (endTitles != null)
                return;
            Map<EndType, Image> images = new EnumMap<>(EndType.class);
            for (EndType type : EndType.values())
                images.put(type, new Image(String.format("/titles/%s.png", type.name().toLowerCase(Locale.ROOT))));
            endTitles = Collections.unmodifiableMap(images);
        }
    }

    /**
     * Initializes the islands.
     */
    private static void initializeIslands() {
        synchronized (islandsLock) {
            if (islands != null)
                return;
            List<Image> images = new ArrayList<>();
            for (int index = 1; index <= 3; index++)
                images.add(new Image(String.format("/realm/islands/%d.png", index)));
            islands = Collections.unmodifiableList(images);
        }
    }

    /**
     * Initializes the professors.
     */
    private static void initializeProfessors() {
        synchronized (professorsLock) {
            if (professors != null)
                return;
            Map<HouseColor, Image> images = new EnumMap<>(HouseColor.class);
            for (HouseColor houseColor : HouseColor.values())
                images.put(houseColor, new Image(String.format("/pawns/professors/%s.png", houseColor.name().toLowerCase(Locale.ROOT))));
            professors = Collections.unmodifiableMap(images);
        }
    }

    /**
     * Initializes the students (realm version).
     */
    private static void initializeStudentsRealm() {
        synchronized (studentsRealmLock) {
            if (studentsRealm != null)
                return;
            Map<HouseColor, Image> images = new EnumMap<>(HouseColor.class);
            for (HouseColor houseColor : HouseColor.values())
                images.put(houseColor, new Image(String.format("/pawns/students/realm/%s.png", houseColor.name().toLowerCase(Locale.ROOT))));
            studentsRealm = Collections.unmodifiableMap(images);
        }
    }

    /**
     * Initializes the students (board version).
     */
    private static void initializeStudentsBoard() {
        synchronized (studentsBoardLock) {
            if (studentsBoard != null)
                return;
            Map<HouseColor, Image> images = new EnumMap<>(HouseColor.class);
            for (HouseColor houseColor : HouseColor.values())
                images.put(houseColor, new Image(String.format("/pawns/students/board/%s.png", houseColor.name().toLowerCase(Locale.ROOT))));
            studentsBoard = Collections.unmodifiableMap(images);
        }
    }

    /**
     * Initializes the special character.
     */
    private static void initializeSpecialCharacters() {
        synchronized (specialCharactersLock) {
            if (specialCharacters != null)
                return;
            List<Image> images = new ArrayList<>();
            List<String> characters = List.of(
                    "monk",
                    "farmer",
                    "herald",
                    "messenger",
                    "herbalist",
                    "centaur",
                    "jester",
                    "knight",
                    "mushroomer",
                    "ministrel",
                    "princess",
                    "thief"
            );
            for (String character : characters)
                images.add(new Image(String.format("/cards/characters/%s.png", character)));
            specialCharacters = Collections.unmodifiableList(images);
        }
    }

    /**
     * Initializes the towers (board version).
     */
    private static void initializeTowersBoard() {
        synchronized (towersBoardLock) {
            if (towersBoard != null)
                return;
            Map<TowerType, Image> images = new EnumMap<>(TowerType.class);
            for (TowerType tower : TowerType.values())
                images.put(tower, new Image(String.format("/pawns/towers/board/%s.png", tower.name().toLowerCase(Locale.ROOT))));
            towersBoard = Collections.unmodifiableMap(images);
        }
    }

    /**
     * Initializes the towers (realm version).
     */
    private static void initializeTowersRealm() {
        synchronized (towersRealmLock) {
            if (towersRealm != null)
                return;
            Map<TowerType, Image> images = new EnumMap<>(TowerType.class);
            for (TowerType tower : TowerType.values())
                images.put(tower, new Image(String.format("/pawns/towers/realm/%s.png", tower.name().toLowerCase(Locale.ROOT))));
            towersRealm = Collections.unmodifiableMap(images);
        }
    }

    /**
     * Initializes the wizards.
     */
    private static void initializeWizards() {
        synchronized (wizardsLock) {
            if (wizards != null)
                return;
            Map<WizardType, Image> images = new EnumMap<>(WizardType.class);
            for (WizardType wizard : WizardType.values())
                images.put(wizard, new Image(String.format("/cards/wizards/%s.png", wizard.name().toLowerCase(Locale.ROOT))));
            wizards = Collections.unmodifiableMap(images);
        }
    }
}
