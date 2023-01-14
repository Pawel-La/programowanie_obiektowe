package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Class extends Application and is a main class that allows us to use javaFX successfully
 */
public class App extends Application {
    private int mapWidth;
    //    mapWidth - width of map
    private int mapHeight;
    //    mapHeight - height of map
    private int initNumberOfGrasses;
    //    initNumberOfGrasses - number of grasses at the start of simulation
    private int grassEnergy;
    //    grassEnergy - energy that single grass gives to animal
    private int grassesDaily;
    //    grassesDaily - number of grasses that grow up every day of simulation
    private IGrassVariant grassVariant;
    //    grassVariant - variant of grasses growing process (deciding which spots are preferred)
    private int initNumberOfAnimals;
    //    initNumberOfAnimals - number of animals at the beginning of simulation
    private int animalEnergy;
    //    animalEnergy- energy of an animal at the start of simulation
    private int fedEnergy;
    //    fedEnergy - energy that animal need to be fed and able to reproduce
    private int childEnergy;
    //    childEnergy - energy that animals spend in total to bore a child, also the initial energy of a child
    private int minMutations;
    //    minMutations - minimal number of mutations to child's genes
    private int maxMutations;
    //    maxMutations - maximal number of mutations to child's genes
    private IMutationVariant mutationVariant;
    //    mutationVariant - variant of mutation (how child's genes should mutate)
    private int numberOfGenes;
    //    numberOfGenes - number of animals' genes (length of animals' genome)
    private IMapVariant mapVariant;
    //    mapVariant - variant of the map (what should happen when animal try to move beyond the edge of map)
    private IBehaviorVariant behaviorVariant;
    //    behaviorVariant - animals behaviour variant (how should animals choose their next move)
    private boolean saveToFile;
    private int moveDelay;
    //    moveDelay - time delay (in milliseconds) between moves in simulation (set up to 300ms)
    private int numOfFile;
    //    numOfFile - number of simulations started (to know where to write a CSV file)

    /**
     * Reset the state of given gridPane
     *
     * @param gridPane - gridPane that certain simulation operate on
     */
    private void reset(GridPane gridPane) {
        gridPane.setGridLinesVisible(false);
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getChildren().clear();
        gridPane.setGridLinesVisible(true);
    }

    private void setUpGrid(GridPane gridPane, Vector2d lowerLeft, Vector2d upperRight) {
//            calculating and setting up width and height of grid to show
        int gridWidth = upperRight.x - lowerLeft.x + 2;
        int gridHeight = upperRight.y - lowerLeft.y + 2;
//            columnSize - size of gridPane's column
        int columnSize = 40;
//            rowSize - size of gridPane's row
        int rowSize = 40;
//            adding rows and columns to the grid
        for (int i1 = 0; i1 < gridWidth; i1++)
            gridPane.getColumnConstraints().add(new ColumnConstraints(columnSize));
        for (int i1 = 0; i1 < gridHeight; i1++)
            gridPane.getRowConstraints().add(new RowConstraints(rowSize));
//            marking upper left corner with y/x=
        Label label = new Label("y/x");
        GridPane.setHalignment(label, HPos.CENTER);
        gridPane.add(label, 0, 0, 1, 1);
//            adding rows indexes to the grid
        for (int i1 = 1; i1 < gridHeight; i1++) {
            label = new Label(Integer.toString(upperRight.y - i1 + 1));
            GridPane.setHalignment(label, HPos.CENTER);
            gridPane.add(label, 0, i1, 1, 1);
        }
//            adding column indexes to the grid
        for (int i1 = 1; i1 < gridWidth; i1++) {
            label = new Label(Integer.toString(lowerLeft.x + i1 - 1));
            GridPane.setHalignment(label, HPos.CENTER);
            gridPane.add(label, i1, 0, 1, 1);
        }
    }

    /**
     * Update the state of given gridPane to actual state of given map
     *
     * @param gridPane - gridPane that certain simulation operate on
     * @param map      - map that the simulation operate on
     */
    public void update(GridPane gridPane, IWorldMap map) {
//        we create new thread to run update on
        Thread mapThread = new Thread(() -> Platform.runLater(() -> {
//            first we clear gridPane state
            reset(gridPane);
//            getting corners of the map, so we can draw the whole map
            Vector2d lowerLeft = map.getLowerLeftMapCorner();
            Vector2d upperRight = map.getUpperRightMapCorner();
            int gridWidth = upperRight.x - lowerLeft.x + 2;
            int gridHeight = upperRight.y - lowerLeft.y + 2;
//            setting up grid basic look
            setUpGrid(gridPane, lowerLeft, upperRight);
//            adding objects to map
            for (int i1 = 1; i1 < gridWidth; i1++) {
                for (int j = 1; j < gridHeight; j++) {
                    Vector2d position = new Vector2d(lowerLeft.x + i1 - 1, upperRight.y - j + 1);
                    if (!map.isOccupied(position))
                        continue;
                    IMapElement mapElement = (IMapElement) map.objectAt(position);
                    GuiElementBox guiElementBox;
                    try {
                        guiElementBox = new GuiElementBox(mapElement);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    gridPane.add(guiElementBox.getVBox(), i1, j, 1, 1);
                }
            }
        }));
        mapThread.start();
    }

    /**
     * setting up default configs, if a config is not changed by user it is set to value set here
     */
    private void setDefaultConfigs() {
        mapWidth = 5;
        mapHeight = 5;
        initNumberOfGrasses = 5;
        grassEnergy = 5;
        grassesDaily = 1;
        grassVariant = new WoodedEquators(mapWidth, mapHeight);
        initNumberOfAnimals = 5;
        animalEnergy = 10;
        fedEnergy = 6;
        childEnergy = 6;
        minMutations = 0;
        maxMutations = 3;
        mutationVariant = new CompletelyRandomMutation();
        numberOfGenes = 3;
        mapVariant = new Globe(mapWidth, mapHeight);
        behaviorVariant = new CompletePredestination();
        saveToFile = false;
        moveDelay = 300;
        numOfFile = 0;
    }

    /**
     * returns map with configurations from file
     *
     * @param configPath - file path of configurations
     * @return map with configurations
     * @throws FileNotFoundException if configPath is not existing
     * @throws RuntimeException      - thrown if any line in file has less than two words
     */
    private Map<String, String> getConfigsFromFile(String configPath)
            throws FileNotFoundException, RuntimeException {
//        creating hashmap for keys parameters and value of these parameters
        Map<String, String> configs = new HashMap<>();
        File config = new File(configPath);
        Scanner scanner = new Scanner(config);
//        reading every line of file, setting first word to key and second word to value
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] words = line.split(" ", 2);
            if (words.length < 2)
                throw new RuntimeException("Wrong config file!");
            configs.computeIfAbsent(words[0], k -> words[1]);
        }
        return configs;
    }

    /**
     * Setting configuration from file which contains pairs of key value in each line
     *
     * @param configs - map with configurations
     */
    private void setConfigs(Map<String, String> configs) {
//        checking all numeric parameters
//        if any key and value was set in configs then change value of parameter
//        if parameter not a number, then throws an exception
        try {
            if (configs.get("mapWidth") != null)
                mapWidth = Integer.parseInt(configs.get("mapWidth"));
            if (configs.get("mapHeight") != null)
                mapHeight = Integer.parseInt(configs.get("mapHeight"));
            if (configs.get("initNumberOfGrasses") != null)
                initNumberOfGrasses = Integer.parseInt(configs.get("initNumberOfGrasses"));
            if (configs.get("grassEnergy") != null)
                grassEnergy = Integer.parseInt(configs.get("grassEnergy"));
            if (configs.get("grassesDaily") != null)
                grassesDaily = Integer.parseInt(configs.get("grassesDaily"));
            if (configs.get("initNumberOfAnimals") != null)
                initNumberOfAnimals = Integer.parseInt(configs.get("initNumberOfAnimals"));
            if (configs.get("animalEnergy") != null)
                animalEnergy = Integer.parseInt(configs.get("animalEnergy"));
            if (configs.get("fedEnergy") != null)
                fedEnergy = Integer.parseInt(configs.get("fedEnergy"));
            if (configs.get("childEnergy") != null)
                childEnergy = Integer.parseInt(configs.get("childEnergy"));
            if (configs.get("minMutations") != null)
                minMutations = Integer.parseInt(configs.get("minMutations"));
            if (configs.get("maxMutations") != null)
                maxMutations = Integer.parseInt(configs.get("maxMutations"));
            if (configs.get("numberOfGenes") != null)
                numberOfGenes = Integer.parseInt(configs.get("numberOfGenes"));
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
//        checking non-numeric parameters (variants)
//        if any variant was set then change value of variant parameter
        if (configs.get("grassVariant") != null)
            switch (configs.get("grassVariant")) {
                case "WoodedEquators" -> grassVariant = new WoodedEquators(mapWidth, mapHeight);
                case "ToxicCorpses" -> grassVariant = new ToxicCorpses(mapWidth, mapHeight);
            }
        if (configs.get("mutationVariant") != null)
            switch (configs.get("mutationVariant")) {
                case "MinorCorrectionMutation" -> mutationVariant = new MinorCorrectionMutation();
                case "CompletelyRandomMutation" -> mutationVariant = new CompletelyRandomMutation();
            }
        if (configs.get("mapVariant") != null)
            switch (configs.get("mapVariant")) {
                case "Globe" -> mapVariant = new Globe(mapWidth, mapHeight);
                case "HellPortal" -> mapVariant = new HellPortal(mapWidth, mapHeight, childEnergy);
            }
        if (configs.get("behaviorVariant") != null)
            switch (configs.get("behaviorVariant")) {
                case "CompletePredestination" -> behaviorVariant = new CompletePredestination();
                case "LittleBitOfCraziness" -> behaviorVariant = new LittleBitOfCraziness();
            }
        if (configs.get("saveToFile") != null)
            saveToFile = configs.get("saveToFile").equals("true");
//        checking if all given parameters are correct and okay for application to run
        checkIfConfigsAreCorrect();
    }

    /**
     * Checking all parameters values if they are correct and if those parameters are okay for app to run.
     * If anything is wrong with parameters, then throws an exception
     *
     * @throws RuntimeException - if parameters can't work, throws exception with accurate comment about what went wrong.
     */
    private void checkIfConfigsAreCorrect() throws RuntimeException {
        if (mapWidth <= 0)
            throw new RuntimeException(
                    "parameter mapWidth value must be positive!");
        if (mapHeight <= 0)
            throw new RuntimeException(
                    "parameter mapHeight value must be positive!");
        if (initNumberOfGrasses < 0)
            throw new RuntimeException(
                    "parameter initNumberOfGrasses value can't be negative!");
        if (grassEnergy < 0)
            throw new RuntimeException(
                    "parameter grassEnergy value can't be negative!");
        if (grassesDaily < 0)
            throw new RuntimeException(
                    "parameter grassesDaily value can't be negative!");
        if (initNumberOfAnimals < 0)
            throw new RuntimeException(
                    "parameter initNumberOfAnimals value can't be negative!");
        if (animalEnergy < 0)
            throw new RuntimeException(
                    "parameter animalEnergy value can't be negative!");
        if (fedEnergy < 0)
            throw new RuntimeException(
                    "parameter fedEnergy value can't be negative!");
        if (childEnergy <= 0)
            throw new RuntimeException(
                    "parameter childEnergy value must be positive!");
        if (minMutations < 0)
            throw new RuntimeException(
                    "parameter minMutations value can't be negative!");
        if (maxMutations < 0)
            throw new RuntimeException(
                    "parameter maxMutations value can't be negative!");
        if (numberOfGenes <= 0)
            throw new RuntimeException(
                    "parameter numberOfGenes value must be positive!");
        if (mapWidth * mapHeight < initNumberOfGrasses)
            throw new RuntimeException(
                    "area of the map can't be smaller than initial number of grasses");
        if (mapWidth * mapHeight < initNumberOfAnimals)
            throw new RuntimeException(
                    "area of the map can't be smaller than initial number of animals");
        if (fedEnergy * 2 < childEnergy)
            throw new RuntimeException(
                    "parents' energy in total during reproduction can't be smaller than child energy");
        if (minMutations > maxMutations)
            throw new RuntimeException(
                    "maximal number of mutations can't be lower than minimal number of mutations");
        if (numberOfGenes < maxMutations)
            throw new RuntimeException("maximal number of mutations can't be higher than number of animals' genes");
    }

    /**
     * Initial function setting up configs to default
     */
    @Override
    public void init() {
        setDefaultConfigs();
    }

    /**
     * Creates new map, creates new engine and new thread for engine, then runs new created thread
     *
     * @param gridPane - gridPane of certain simulation
     */
    private void go(GridPane gridPane) {
        numOfFile++;
        IWorldMap map = new WorldMap(mapVariant, initNumberOfGrasses, grassEnergy, grassesDaily,
                mapWidth, mapHeight, childEnergy, fedEnergy, minMutations,
                maxMutations, mutationVariant, behaviorVariant, grassVariant);
        SimulationEngine engine = new SimulationEngine(
                map, initNumberOfAnimals, numOfFile, this, saveToFile, gridPane,
                moveDelay, animalEnergy, numberOfGenes, behaviorVariant);
        Thread engineThread = new Thread(engine);
        engineThread.start();
    }

    /**
     * Creates first stage and scene, if any button is clicked then set up new stage and gridPane.
     * Also, either set up saved configs or get configs from user
     *
     * @param primaryStage - primary stage
     */
    @Override
    public void start(Stage primaryStage) {

        GridPane gridPane = new GridPane();
        Button button1 = new Button("Config 1");
        Button button2 = new Button("Config 2");
        Button button3 = new Button("Own Config File");
        HBox hBox = new HBox(button1, button2, button3);
        VBox vBox = new VBox(hBox, gridPane);
        Scene scene = new Scene(vBox, 500, 500);

        button1.setOnAction(value -> {
            try {
                setConfigs(getConfigsFromFile("src/main/resources/config1.txt"));
                GridPane gridPane1 = new GridPane();
                VBox vBox1 = new VBox(hBox, gridPane1);
                Scene scene1 = new Scene(vBox1, 500, 500);
                Stage stage = new Stage();
                stage.setScene(scene1);
                stage.show();
                go(gridPane1);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        button2.setOnAction(value -> {
            try {
                setConfigs(getConfigsFromFile("src/main/resources/config2.txt"));
                GridPane gridPane1 = new GridPane();
                VBox vBox1 = new VBox(hBox, gridPane1);
                Scene scene1 = new Scene(vBox1, 500, 500);
                Stage stage = new Stage();
                stage.setScene(scene1);
                stage.show();
                go(gridPane1);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        button3.setOnAction(value -> {
            GridPane gridPane1 = new GridPane();

            gridPane1.add(new Label("map width: "), 0, 0);
            TextField mapWidthTF = new TextField();
            gridPane1.add(mapWidthTF, 1, 0);

            gridPane1.add(new Label("map height: "), 0, 1);
            TextField mapHeightTF = new TextField();
            gridPane1.add(mapHeightTF, 1, 1);

            gridPane1.add(new Label("initial number of grasses: "), 0, 2);
            TextField initNumberOfGrassesTF = new TextField();
            gridPane1.add(initNumberOfGrassesTF, 1, 2);

            gridPane1.add(new Label("energy that grass gives: "), 0, 3);
            TextField grassEnergyTF = new TextField();
            gridPane1.add(grassEnergyTF, 1, 3);

            gridPane1.add(new Label("number of grasses that grow daily: "), 0, 4);
            TextField grassesDailyTF = new TextField();
            gridPane1.add(grassesDailyTF, 1, 4);

            gridPane1.add(new Label("grass variant:"), 0, 5);
            ChoiceBox<String> grassVariantCB = new ChoiceBox<>();
            grassVariantCB.getItems().add("Wooded Equators");
            grassVariantCB.getItems().add("Toxic Corpses");
            gridPane1.add(grassVariantCB, 1, 5);

            gridPane1.add(new Label("initial number of animals: "), 0, 6);
            TextField initNumberOfAnimalsTF = new TextField();
            gridPane1.add(initNumberOfAnimalsTF, 1, 6);

            gridPane1.add(new Label("initial animal energy: "), 0, 7);
            TextField animalEnergyTF = new TextField();
            gridPane1.add(animalEnergyTF, 1, 7);

            gridPane1.add(new Label("energy for animal to be considered fed: "), 0, 8);
            TextField fedEnergyTF = new TextField();
            gridPane1.add(fedEnergyTF, 1, 8);

            gridPane1.add(new Label("initial newborn animal energy: "), 0, 9);
            TextField childEnergyTF = new TextField();
            gridPane1.add(childEnergyTF, 1, 9);

            gridPane1.add(new Label("minimal number of mutations: "), 0, 10);
            TextField minMutationsTF = new TextField();
            gridPane1.add(minMutationsTF, 1, 10);

            gridPane1.add(new Label("maximal number of mutations: "), 0, 11);
            TextField maxMutationsTF = new TextField();
            gridPane1.add(maxMutationsTF, 1, 11);

            gridPane1.add(new Label("mutation variant:"), 0, 12);
            ChoiceBox<String> mutationVariantCB = new ChoiceBox<>();
            mutationVariantCB.getItems().add("Completely Random Mutation");
            mutationVariantCB.getItems().add("Minor Correction Mutation");
            gridPane1.add(mutationVariantCB, 1, 12);

            gridPane1.add(new Label("length of animal genome: "), 0, 13);
            TextField numberOfGenesTF = new TextField();
            gridPane1.add(numberOfGenesTF, 1, 13);

            gridPane1.add(new Label("map variant(edge service):"), 0, 14);
            ChoiceBox<String> mapVariantCB = new ChoiceBox<>();
            mapVariantCB.getItems().add("Globe");
            mapVariantCB.getItems().add("Hell Portal");
            gridPane1.add(mapVariantCB, 1, 14);

            gridPane1.add(new Label("map variant(edge service):"), 0, 15);
            ChoiceBox<String> behaviorVariantCB = new ChoiceBox<>();
            behaviorVariantCB.getItems().add("Complete Predestination");
            behaviorVariantCB.getItems().add("Little Bit Of Craziness");
            gridPane1.add(behaviorVariantCB, 1, 15);

            CheckBox checkBox = new CheckBox("save simulation to csv file");
            gridPane1.add(checkBox, 0, 16);

            Button button = new Button("Save configs");
            gridPane1.add(button, 0, 17);

            Scene scene1 = new Scene(gridPane1, 500, 500);
            Stage stage = new Stage();

            button.setOnAction(e -> {
                Map<String, String> configs = new HashMap<>();
                if (!mapWidthTF.getText().isEmpty())
                    configs.put("mapWidth", mapWidthTF.getText());
                if (!mapHeightTF.getText().isEmpty())
                    configs.put("mapHeight", mapHeightTF.getText());
                if (!initNumberOfGrassesTF.getText().isEmpty())
                    configs.put("initNumberOfGrasses", initNumberOfGrassesTF.getText());
                if (!grassEnergyTF.getText().isEmpty())
                    configs.put("grassEnergy", grassEnergyTF.getText());
                if (!grassesDailyTF.getText().isEmpty())
                    configs.put("grassesDaily", grassesDailyTF.getText());
                if (grassVariantCB.getValue() != null) {
                    if (grassVariantCB.getValue().equals("Wooded Equators"))
                        configs.put("grassVariant", "WoodedEquators");
                    else
                        configs.put("grassVariant", "ToxicCorpses");
                }
                if (!initNumberOfAnimalsTF.getText().isEmpty())
                    configs.put("initNumberOfAnimals", initNumberOfAnimalsTF.getText());
                if (!animalEnergyTF.getText().isEmpty())
                    configs.put("animalEnergy", animalEnergyTF.getText());
                if (!fedEnergyTF.getText().isEmpty())
                    configs.put("fedEnergy", fedEnergyTF.getText());
                if (!childEnergyTF.getText().isEmpty())
                    configs.put("childEnergy", childEnergyTF.getText());
                if (!minMutationsTF.getText().isEmpty())
                    configs.put("minMutations", minMutationsTF.getText());
                if (!maxMutationsTF.getText().isEmpty())
                    configs.put("maxMutations", maxMutationsTF.getText());
                if (mutationVariantCB.getValue() != null) {
                    if (mutationVariantCB.getValue().equals("Completely Random Mutation"))
                        configs.put("mutationVariant", "CompletelyRandomMutation");
                    else
                        configs.put("mutationVariant", "MinorCorrectionMutation");
                }
                if (!numberOfGenesTF.getText().isEmpty())
                    configs.put("numberOfGenes", numberOfGenesTF.getText());
                if (mapVariantCB.getValue() != null) {
                    if (mapVariantCB.getValue().equals("Globe"))
                        configs.put("mapVariant", "Globe");
                    else
                        configs.put("mapVariant", "HellPortal");
                }
                if (behaviorVariantCB.getValue() != null) {
                    if (behaviorVariantCB.getValue().equals("Complete Predestination"))
                        configs.put("behaviorVariant", "CompletePredestination");
                    else
                        configs.put("behaviorVariant", "LittleBitOfCraziness");
                }
                if (checkBox.isSelected()) {
                    configs.put("saveToFile", "true");
                }

                setConfigs(configs);
                gridPane1.getChildren().clear();
                VBox vBox1 = new VBox(hBox, gridPane1);
                Scene scene2 = new Scene(vBox1, 500, 500);
                stage.setScene(scene2);
                go(gridPane1);
            });

            stage.setScene(scene1);
            stage.show();
        });

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
