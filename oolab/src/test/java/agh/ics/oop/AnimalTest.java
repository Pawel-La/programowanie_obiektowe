package agh.ics.oop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnimalTest {
    private final String errorMessage = "Test failed!";

    @Test
    public void positionAndOrientationTest(){
        Animal animal = new Animal();
//        (2, 2) north
        animal.move(MoveDirection.FORWARD);
//        (2, 3) north
        Assertions.assertEquals(animal.getPosition(), new Vector2d(2,3), errorMessage);
        animal.move(MoveDirection.BACKWARD);
//        (2, 2) north
        Assertions.assertEquals(animal.getPosition(), new Vector2d(2, 2), errorMessage);
        Assertions.assertEquals(animal.getOrientation(), MapDirection.NORTH, errorMessage);
        animal.move(MoveDirection.RIGHT);
//        (2, 2) east
        Assertions.assertEquals(animal.getOrientation(), MapDirection.EAST, errorMessage);
        animal.move(MoveDirection.LEFT);
//        (2, 2) north
        Assertions.assertEquals(animal.getOrientation(), MapDirection.NORTH, errorMessage);
    }

    @Test
    public void outOfMapTest(){
        Animal animal = new Animal();
//        (2, 2) north
        animal.move(MoveDirection.FORWARD);
//        (2, 3) north
        animal.move(MoveDirection.FORWARD);
//        (2,4) north
        animal.move(MoveDirection.FORWARD);
//        (2,4) north
        Assertions.assertEquals(animal.getPosition(), new Vector2d(2,4), errorMessage);
        animal.move(MoveDirection.RIGHT);
//        (2,4) east
        animal.move(MoveDirection.FORWARD);
//        (3,4) east
        animal.move(MoveDirection.FORWARD);
//        (4,4) east
        animal.move(MoveDirection.FORWARD);
//        (4,4) east
        Assertions.assertEquals(animal.getPosition(), new Vector2d(4,4), errorMessage);
        animal.move(MoveDirection.BACKWARD);
//        (3,4) east
        animal.move(MoveDirection.LEFT);
//        (3,4) north
        animal.move(MoveDirection.FORWARD);
//        (3,4) north
        Assertions.assertEquals(animal.getPosition(), new Vector2d(3,4), errorMessage);
    }
}
