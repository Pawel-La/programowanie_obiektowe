package agh.ics.oop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GrassFieldTest {
    @Test
    public void testAnimalEatsGrass(){
        IWorldMap map = new GrassField(10);
        Animal animal1 = new Animal(map);
        map.place(animal1);
        System.out.println(map);
        animal1.move(MoveDirection.FORWARD);
        animal1.move(MoveDirection.FORWARD);
        animal1.move(MoveDirection.FORWARD);
        animal1.move(MoveDirection.FORWARD);
        animal1.move(MoveDirection.FORWARD);
        animal1.move(MoveDirection.FORWARD);
        animal1.move(MoveDirection.FORWARD);
        animal1.move(MoveDirection.FORWARD);
        animal1.move(MoveDirection.FORWARD);
        animal1.move(MoveDirection.FORWARD);
        System.out.println(map);
    }
    @Test
    public void canMoveToTest(){
        IWorldMap map = new GrassField(10);
        Animal animal1 = new Animal(map, new Vector2d(2,3));
        Animal animal2 = new Animal(map, new Vector2d(5,5));
        Animal animal3 = new Animal(map, new Vector2d(1,7));
        map.place(animal1);
        map.place(animal2);
        map.place(animal3);
        Assertions.assertFalse(map.canMoveTo(new Vector2d(2,3)));
        Assertions.assertFalse(map.canMoveTo(new Vector2d(5,5)));
        Assertions.assertFalse(map.canMoveTo(new Vector2d(1,7)));
        Assertions.assertTrue(map.canMoveTo(new Vector2d(1, 4)));
        Assertions.assertTrue(map.canMoveTo(new Vector2d(0, 0)));
        Assertions.assertTrue(map.canMoveTo(new Vector2d(0, 1)));
        Assertions.assertTrue(map.canMoveTo(new Vector2d(0, 2)));
        Assertions.assertTrue(map.canMoveTo(new Vector2d(0, 3)));
        Assertions.assertTrue(map.canMoveTo(new Vector2d(0, 4)));
        Assertions.assertTrue(map.canMoveTo(new Vector2d(0, 5)));
        Assertions.assertTrue(map.canMoveTo(new Vector2d(-1, 4)));
        Assertions.assertTrue(map.canMoveTo(new Vector2d(10, 4)));
        Assertions.assertTrue(map.canMoveTo(new Vector2d(1000000000, 4)));
    }

    @Test
    public void placeIsOccupiedObjectAtTest() {
        IWorldMap map = new GrassField(10);
        Animal animal1 = new Animal(map, new Vector2d(2,2));
        Animal animal2 = new Animal(map, new Vector2d(2,2));
        Animal animal3 = new Animal(map, new Vector2d(10,4));
        Animal animal4 = new Animal(map, new Vector2d(-10,-4));

        Assertions.assertTrue(map.place(animal1));
        Assertions.assertFalse(map.place(animal2));
        Assertions.assertTrue(map.place(animal3));
        Assertions.assertTrue(map.place(animal4));
        Assertions.assertTrue(map.isOccupied(new Vector2d(2,2)));
        Assertions.assertEquals(map.objectAt(new Vector2d(2,2)), animal1);
    }

    @Test
    public void generalTest(){
        IWorldMap map = new GrassField(10);
        Animal animal1 = new Animal(map, new Vector2d(2,2));
        Animal animal2 = new Animal(map, new Vector2d(2,4));
        Animal animal3 = new Animal(map, new Vector2d(10,4));
        Animal animal4 = new Animal(map, new Vector2d(-10,-4));
        map.place(animal1);
        map.place(animal2);
        map.place(animal3);
        map.place(animal4);
        animal1.move(MoveDirection.FORWARD);
        Assertions.assertEquals(map.objectAt(new Vector2d(2,3)), animal1);
        animal1.move(MoveDirection.FORWARD);
        Assertions.assertEquals(map.objectAt(new Vector2d(2,3)), animal1);
    }

}
