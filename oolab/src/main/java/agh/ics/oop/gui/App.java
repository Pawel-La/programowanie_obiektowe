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
import java.io.FileNotFoundException;

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

    @Override
    public void init() {
        mapWidth = 5;
        mapHeight = 5;
        initNumberOfGrasses = 10;
        grassEnergy = 5;
//        grassesDaily = 5;
//        grassVariant =;
        initNumberOfAnimals = 7;
        animalEnergy = 10;
        fedEnergy = 6;
        childEnergy = 8;
        minMutations = 0;
        maxMutations = 10;
        mutationVariant = new CompletelyRandomMutation();
        numberOfGenes = 10;
        mapVariant = new Globe(mapWidth, mapHeight);
        behaviorVariant = new CompletePredestination();
        moveDelay = 300;
    }

    @Override
    public void start(Stage primaryStage){

        Button button = new Button("Start");
        HBox hBox = new HBox(button);
        VBox vBox = new VBox(hBox, gridPane);

        Scene scene = new Scene(vBox, 500, 500);

        button.setOnAction(value ->  {
            map = new GrassField(mapVariant, initNumberOfGrasses,
                    grassEnergy, mapWidth, mapHeight, childEnergy, fedEnergy,
                    minMutations, maxMutations, mutationVariant, behaviorVariant);
            SimulationEngine engine = new SimulationEngine(
                    map, initNumberOfAnimals, this,
                    moveDelay, animalEnergy, numberOfGenes, behaviorVariant);
            Thread engineThread = new Thread(engine);
            engineThread.start();
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
