package agh.ics.oop;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimalTest {
    @Test
    public void test(){
//        IWorldMap map = new GrassField(5);
//        IBehaviorVariant behaviorVariant = new CompletePredestination();
//        Animal animal = new Animal(map, new Vector2d(0,0), 10, 10, behaviorVariant);
    }
//    private final String errorMessage = "Test failed!";

}
//    @Test
//    public void positionAndOrientationTest() {
//        IWorldMap map = new RectangularMap(5, 5);
//        Vector2d initialPosition = new Vector2d(2, 2);
//        Animal animal = new Animal(map, initialPosition);
//        animal.move(MoveDirection.FORWARD);
//        Assertions.assertEquals(animal.getPosition(), new Vector2d(2, 3), errorMessage);
//        Assertions.assertTrue(animal.isAt(new Vector2d(2, 3)), errorMessage);
//        animal.move(MoveDirection.BACKWARD);
//        Assertions.assertEquals(animal.getPosition(), new Vector2d(2, 2), errorMessage);
//        Assertions.assertTrue(animal.isAt(new Vector2d(2, 2)), errorMessage);
//        Assertions.assertEquals(animal.getOrientation(), MapDirection.NORTH, errorMessage);
//        animal.move(MoveDirection.RIGHT);
//        Assertions.assertEquals(animal.getOrientation(), MapDirection.EAST, errorMessage);
//        animal.move(MoveDirection.LEFT);
//        Assertions.assertEquals(animal.getOrientation(), MapDirection.NORTH, errorMessage);
//    }
//    @Test
//    public void outOfMapTest(){
//        IWorldMap map = new RectangularMap(5,5);
//        Vector2d initialPosition = new Vector2d(2,2);
//        Animal animal = new Animal(map, initialPosition);
//        animal.move(MoveDirection.FORWARD);
//        animal.move(MoveDirection.FORWARD);
//        animal.move(MoveDirection.FORWARD);
//        Assertions.assertEquals(animal.getPosition(), new Vector2d(2,4), errorMessage);
//        Assertions.assertTrue(animal.isAt(new Vector2d(2,4)));
//        animal.move(MoveDirection.RIGHT);
//        animal.move(MoveDirection.FORWARD);
//        animal.move(MoveDirection.FORWARD);
//        animal.move(MoveDirection.FORWARD);
//        Assertions.assertEquals(animal.getPosition(), new Vector2d(4,4), errorMessage);
//        Assertions.assertTrue(animal.isAt(new Vector2d(4,4)));
//        animal.move(MoveDirection.BACKWARD);
//        animal.move(MoveDirection.LEFT);
//        animal.move(MoveDirection.FORWARD);
//        Assertions.assertEquals(animal.getPosition(), new Vector2d(3,4), errorMessage);
//    }
//
//    @Test
//    public void toStringTest(){
//        IWorldMap map = new RectangularMap(5,5);
//        Vector2d initialPosition = new Vector2d(2,2);
//        Animal animal = new Animal(map, initialPosition);
//        Assertions.assertEquals(animal.toString(), "N");
//        animal.move(MoveDirection.RIGHT);
//        Assertions.assertEquals(animal.toString(), "E");
//        animal.move(MoveDirection.RIGHT);
//        Assertions.assertEquals(animal.toString(), "S");
//        animal.move(MoveDirection.RIGHT);
//        Assertions.assertEquals(animal.toString(), "W");
//    }
//
//}
