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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App extends Application{
    private GrassField map;
    private int mapWidth;
    private int mapHeight;
    //    number of grasses at the beginning of simulation
    private int initNumberOfGrasses;
    //    energy that single grass gives
    private int grassEnergy;
    //    number of grasses that raise daily
    private int grassesDaily;
    private IGrassVariant grassVariant;
    //    number of animals at the beginning of simulation
    private int initNumberOfAnimals;
    //    energy of an animal at the start of simulation
    private int animalEnergy;
    //    energy that animal need to be able to reproduce
    private int fedEnergy;
    //    energy that animals spend to bore a child,
    //    also the initial energy of a child
    private int childEnergy;
    //    minimal number of mutations to child's genomes
    private int minMutations;
    //    maximal number of mutations to child's genomes
    private int maxMutations;
    private IMutationVariant mutationVariant;
    //    length of animals' genome
    private int numberOfGenes;
    private IMapVariant mapVariant;
    //    animals behaviour variant
    private IBehaviorVariant behaviorVariant;
    //   some other app's parameters
    private final GridPane gridPane = new GridPane();
    private int moveDelay;
    private Vector2d lowerLeft;
    private Vector2d upperRight;

    private void reset(){
        gridPane.setGridLinesVisible(false);
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getChildren().clear();
        gridPane.setGridLinesVisible(true);
    }

    public void update(){
        Thread mapThread = new Thread(() -> Platform.runLater(() -> {
            reset();
            lowerLeft = map.getDrawingLowerLeft();
            upperRight = map.getDrawingUpperRight();
            //        calculating width and height of map to show
            final int gridWidth = upperRight.x - lowerLeft.x + 2;
            final int gridHeight = upperRight.y - lowerLeft.y + 2;
            final int columnSize = 40;
            final int rowSize = 40;
            //        adding rows and columns
            for (int i1 = 0; i1 < gridWidth; i1++)
                gridPane.getColumnConstraints().add(new ColumnConstraints(columnSize));
            for (int i1 = 0; i1 < gridHeight; i1++)
                gridPane.getRowConstraints().add(new RowConstraints(rowSize));

            //        mark upper left corner with y/x
            Label label = new Label("y/x");
            GridPane.setHalignment(label, HPos.CENTER);
            gridPane.add(label, 0, 0, 1, 1);

            //        adding rows indexes
            for (int i1 = 1; i1 < gridHeight; i1++) {
                label = new Label(Integer.toString(upperRight.y - i1 + 1));
                GridPane.setHalignment(label, HPos.CENTER);
                gridPane.add(label, 0, i1, 1, 1);
            }
            //        adding column indexes
            for (int i1 = 1; i1 < gridWidth; i1++) {
                label = new Label(Integer.toString(lowerLeft.x + i1 - 1));
                GridPane.setHalignment(label, HPos.CENTER);
                gridPane.add(label, i1, 0, 1, 1);
            }
            //        adding objects to map
            //        __fajnie by bylo uzywac tu elementow z mapElements z mapy tylko jak czy zrobic public w AbstWrldMap??
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
    private void setDefaultConfigs(){
        mapWidth = 7;
        mapHeight = 7;
        initNumberOfGrasses = 5;
        grassEnergy = 5;
        grassesDaily = 1;
        grassVariant = new WoodedEquators(mapWidth, mapHeight, grassesDaily);
        initNumberOfAnimals = 10;
        animalEnergy = 50;
        fedEnergy = 6;
        childEnergy = 8;
        minMutations = 0;
        maxMutations = 10;
        mutationVariant = new CompletelyRandomMutation();
        numberOfGenes = 3;
        mapVariant = new Globe(mapWidth, mapHeight);
        behaviorVariant = new CompletePredestination();
        moveDelay = 300;
    }
    private void setConfigs(String configPath) throws FileNotFoundException {
        Map<String, String> configs = new HashMap<>();
        File config = new File(configPath);
        Scanner scanner = new Scanner(config);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String [] words = line.split(" ", 2);
            if (words.length < 2)
                throw new RuntimeException("Bledny plik konfiguracyjny!");
            configs.computeIfAbsent(words[0], k -> words[1]);
        }
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

        try{
            switch (configs.get("grassVariant")){
                case "WoodedEquators" -> grassVariant = new WoodedEquators(mapWidth, mapHeight, grassesDaily);
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
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        checkIfConfigsAreCorrect();
    }
    private void checkIfConfigsAreCorrect(){
        if (mapWidth <= 0){
            throw new RuntimeException("parametr mapWidth musi byc dodatni!");
        }
        if (mapHeight <= 0){
            throw new RuntimeException("parametr mapHeight musi byc dodatni!");
        }
        if (initNumberOfGrasses < 0){
            throw new RuntimeException("parametr initNumberOfGrasses nie moze byc ujemny!");
        }
        if (grassEnergy < 0){
            throw new RuntimeException("parametr grassEnergy nie moze byc ujemny!");
        }
        if (grassesDaily < 0){
            throw new RuntimeException("parametr grassesDaily nie moze byc ujemny!");
        }
        if (initNumberOfAnimals < 0){
            throw new RuntimeException("parametr initNumberOfAnimals nie moze byc ujemny!");
        }
        if (animalEnergy < 0){
            throw new RuntimeException("parametr animalEnergy nie moze byc ujemny!");
        }
        if (fedEnergy < 0){
            throw new RuntimeException("parametr fedEnergy nie moze byc ujemny!");
        }
        if (childEnergy <= 0){
            throw new RuntimeException("parametr childEnergy musi byc dodatni!");
        }
        if (minMutations < 0){
            throw new RuntimeException("parametr minMutations nie moze byc ujemny!");
        }
        if (maxMutations < 0){
            throw new RuntimeException("parametr maxMutations nie moze byc ujemny!");
        }
        if (numberOfGenes <= 0){
            throw new RuntimeException("parametr numberOfGenes musi byc dodatni!");
        }
        if (mapWidth * mapHeight < initNumberOfGrasses){
            throw new RuntimeException("powierzchnia mapy musi byc wieksza od poczatkowej liczby traw");
        }
        if (mapWidth * mapHeight < initNumberOfAnimals){
            throw new RuntimeException("powierzchnia mapy musi byc wieksza od poczatkowej liczby zwierzat");
        }
        if (fedEnergy * 2 < childEnergy){
            throw new RuntimeException("suma energii rodzicow przy reprodukcji " +
                    "nie moze byc mniejsza od energii dziecka");
        }
        if (minMutations > maxMutations){
            throw new RuntimeException("maksymalna liczba mutacji nie moze byc mniejsza od minimalnej liczby mutacji");
        }
        if (numberOfGenes < maxMutations){
            throw new RuntimeException("maksymalna liczba mutacji nie moze byc wieksza od liczby genow zwierzecia");
        }
    }
    @Override
    public void init() {
        setDefaultConfigs();
    }
    private void go(){
        map = new GrassField(mapVariant, initNumberOfGrasses, grassEnergy, grassesDaily,
                mapWidth, mapHeight, childEnergy, fedEnergy, minMutations,
                maxMutations, mutationVariant, behaviorVariant, grassVariant);
        SimulationEngine engine = new SimulationEngine(
                map, initNumberOfAnimals, this,
                moveDelay, animalEnergy, numberOfGenes, behaviorVariant);
        Thread engineThread = new Thread(engine);
        engineThread.start();
    }
    @Override
    public void start(Stage primaryStage){

        Button button1 = new Button("Konfiguracja 1");
        Button button2 = new Button("Konfiguracja 2");
        Button button3 = new Button("Wlasny plik konfiguracyjny");
        HBox hBox = new HBox(button1, button2, button3);
        VBox vBox = new VBox(hBox, gridPane);

        Scene scene = new Scene(vBox, 500, 500);

        maxMutations = Math.min(maxMutations, numberOfGenes);

        button1.setOnAction(value ->  {
            try {
                setConfigs("src/main/resources/config1.txt");
                go();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        button2.setOnAction(value ->  {
            try {
                setConfigs("src/main/resources/config2.txt");
                go();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        button3.setOnAction(value ->  {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(null);
            try {
                setConfigs(fileChooser.getSelectedFile().getAbsolutePath());
                go();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
