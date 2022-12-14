package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class App extends Application implements IPositionChangeObserver{
    private GrassField map;
    private Vector2d lowerLeft, upperRight;
    private final GridPane gridPane = new GridPane();
    private Vector2d[] positions;
    private IBehaviorVariant behaviorVariant;
    int moveDelay;
    int energy;
    int number_of_genomes;
    protected Map<Vector2d, IMapElement> animals = new HashMap<>();

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
//        this.positions = new Vector2d[]{new Vector2d(2, 2), new Vector2d(3, 4),
//        new Vector2d(6,5), new Vector2d(2,5)};
        this.positions = new Vector2d[]{new Vector2d(2, 2), new Vector2d(2,3),
        new Vector2d(3,2), new Vector2d(3,3)};
        moveDelay = 300;
        energy = 10;
        number_of_genomes = 5;
        behaviorVariant = new CompletePredestination();
    }

    @Override
    public void start(Stage primaryStage){

        Button button = new Button("Start");
        TextField textField = new TextField();
        HBox hBox = new HBox(button, textField);
        VBox vBox = new VBox(hBox, gridPane);

        Scene scene = new Scene(vBox, 500, 500);

        button.setOnAction(value ->  {
            map = new GrassField(10);
            SimulationEngine engine = new SimulationEngine(
                    map, positions, this, moveDelay, energy, number_of_genomes, behaviorVariant);
            Thread engineThread = new Thread(engine);
            engineThread.start();
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        IMapElement animal = animals.get(oldPosition);
        animals.remove(oldPosition);
        animals.put(newPosition, animal);
    }

    public void addNewAnimal(IMapElement mapElement){
        animals.put(mapElement.getPosition(), mapElement);
    }
}
