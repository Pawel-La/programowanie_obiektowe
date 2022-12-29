package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Class extends Application and is a main class that allows us to use javaFX successfully
 */
public class App extends Application{
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
    private int moveDelay;
    //    moveDelay - time delay (in milliseconds) between moves in simulation (set up to 300ms)
    /**
     * Reset the state of given gridPane
     * @param gridPane - gridPane that certain simulation operate on
     */
    private void reset(GridPane gridPane){
        gridPane.setGridLinesVisible(false);
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getChildren().clear();
        gridPane.setGridLinesVisible(true);
    }

    private void setUpGrid(GridPane gridPane, Vector2d lowerLeft, Vector2d upperRight){
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
     * @param gridPane - gridPane that certain simulation operate on
     * @param map - map that the simulation operate on
     */
    public void update(GridPane gridPane, IWorldMap map){
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
            //        adding objects to map
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
    private void setDefaultConfigs(){
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
        moveDelay = 300;
    }

    /**
     * Setting configuration from file which contains pairs of key value in each line
     * @param configPath - file path of the file with configurations
     * @throws FileNotFoundException - thrown if configPath not found
     * @throws RuntimeException - thrown if any line in file has less than two words
     */
    private void setConfigs(String configPath)
            throws FileNotFoundException, RuntimeException {
//        creating hashmap for keys parameters and value of these parameters
        Map<String, String> configs = new HashMap<>();
        File config = new File(configPath);
        Scanner scanner = new Scanner(config);
//        reading every line of file, setting first word to key and second word to value
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String [] words = line.split(" ", 2);
            if (words.length < 2)
                throw new RuntimeException("Wrong config file!");
            configs.computeIfAbsent(words[0], k -> words[1]);
        }
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
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
//        checking non-numeric parameters (variants)
//        if any variant was set then change value of variant parameter
        switch (configs.get("grassVariant")){
            case "WoodedEquators" -> grassVariant = new WoodedEquators(mapWidth, mapHeight);
            case "ToxicCorpses" -> grassVariant = new ToxicCorpses(mapWidth, mapHeight);
        }
        switch (configs.get("mutationVariant")){
            case "MinorCorrectionMutation" -> mutationVariant = new MinorCorrectionMutation();
            case "CompletelyRandomMutation" -> mutationVariant = new CompletelyRandomMutation();
        }
        switch (configs.get("mapVariant")){
            case "Globe" -> mapVariant = new Globe(mapWidth, mapHeight);
            case "HellPortal" -> mapVariant = new HellPortal(mapWidth, mapHeight, childEnergy);
        }
        switch (configs.get("behaviorVariant")){
            case "CompletePredestination" -> behaviorVariant = new CompletePredestination();
            case "LittleBitOfCraziness" -> behaviorVariant = new LittleBitOfCraziness();
        }
//        checking if all given parameters are correct and okay for application to run
        checkIfConfigsAreCorrect();
    }

    /**
     * Checking all parameters values if they are correct and if those parameters are okay for app to run.
     * If anything is wrong with parameters, then throws an exception
     * @throws RuntimeException - if parameters can't work, throws exception with accurate comment about what went wrong.
     */
    private void checkIfConfigsAreCorrect() throws RuntimeException{
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
     * @param gridPane - gridPane of certain simulation
     */
    private void go(GridPane gridPane){
        IWorldMap map = new WorldMap(mapVariant, initNumberOfGrasses, grassEnergy, grassesDaily,
                mapWidth, mapHeight, childEnergy, fedEnergy, minMutations,
                maxMutations, mutationVariant, behaviorVariant, grassVariant);
        SimulationEngine engine = new SimulationEngine(
                map, initNumberOfAnimals, this, gridPane,
                moveDelay, animalEnergy, numberOfGenes, behaviorVariant);
        Thread engineThread = new Thread(engine);
        engineThread.start();
    }
    /**
     * Creates first stage and scene, if any button is clicked then set up new stage and gridPane.
     * Also, either set up saved configs or get configs from user
     * @param primaryStage - primary stage
     */
    @Override
    public void start(Stage primaryStage){

        GridPane gridPane = new GridPane();
        Button button1 = new Button("Config 1");
        Button button2 = new Button("Config 2");
        Button button3 = new Button("Own Config File");
        HBox hBox = new HBox(button1, button2, button3);
        VBox vBox = new VBox(hBox, gridPane);
        Scene scene = new Scene(vBox, 500, 500);

        button1.setOnAction(value ->  {
            try {
                setConfigs("src/main/resources/config1.txt");
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

        button2.setOnAction(value ->  {
            try {
                setConfigs("src/main/resources/config2.txt");
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

        button3.setOnAction(value ->  {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(null);
            try {
                setConfigs(fileChooser.getSelectedFile().getAbsolutePath());
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

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
