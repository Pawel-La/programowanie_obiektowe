package agh.ics.oop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RectangularMapTest {
    @Test
    public void canMoveToTest(){
        IWorldMap map = new RectangularMap(5,6);
        Animal animal1 = new Animal(map, new Vector2d(2,3));
        map.place(animal1);
        Assertions.assertFalse(map.canMoveTo(new Vector2d(2,3)));
        Assertions.assertTrue(map.canMoveTo(new Vector2d(1, 4)));
        Assertions.assertTrue(map.canMoveTo(new Vector2d(0, 0)));
        Assertions.assertFalse(map.canMoveTo(new Vector2d(5, 6)));
        Assertions.assertFalse(map.canMoveTo(new Vector2d(-1, 4)));
        Assertions.assertFalse(map.canMoveTo(new Vector2d(10, 4)));
    }

    @Test
    public void placeIsOccupiedObjectAtTest() {
        IWorldMap map = new RectangularMap(10,4);
        Animal animal1 = new Animal(map, new Vector2d(2,2));
        Animal animal2 = new Animal(map, new Vector2d(2,2));
        Animal animal3 = new Animal(map, new Vector2d(100,2));
        Animal animal4 = new Animal(map, new Vector2d(10,4));

        Assertions.assertTrue(map.place(animal1));
        Assertions.assertFalse(map.place(animal2));
        Assertions.assertFalse(map.place(animal3));
        Assertions.assertFalse(map.place(animal4));
        Assertions.assertTrue(map.isOccupied(new Vector2d(2,2)));
        Assertions.assertFalse(map.isOccupied(new Vector2d(100,2)));
        Assertions.assertEquals(map.objectAt(new Vector2d(2,2)), animal1);
    }
}
