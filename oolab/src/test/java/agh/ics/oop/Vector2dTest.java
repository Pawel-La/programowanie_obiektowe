package agh.ics.oop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Vector2dTest {

    private final String errorMessage = "Test failed!";

    @Test
    public void equalsTest(){
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(1,2);
        Vector2d v3 = new Vector2d(1,1);
        String v4 = "(1,2)";

        Assertions.assertEquals(v1, v2, errorMessage);
        Assertions.assertNotEquals(v1, v3, errorMessage);
        Assertions.assertNotEquals(v1, v4, errorMessage);
    }

    @Test
    public void toStringTest(){
        Vector2d v1 = new Vector2d(2,3);
        Vector2d v2 = new Vector2d(-2,2);
        Vector2d v3 = new Vector2d(-3,-3);

        Assertions.assertEquals(v1.toString(), "(2,3)", errorMessage);
        Assertions.assertEquals(v2.toString(), "(-2,2)", errorMessage);
        Assertions.assertEquals(v3.toString(), "(-3,-3)", errorMessage);
    }

    @Test
    public void precedesTest(){
        Vector2d v1 = new Vector2d(1,1);
        Vector2d v2 = new Vector2d(2,2);
        Vector2d v3 = new Vector2d(2,3);
        Vector2d v4 = new Vector2d(0,3);


        Assertions.assertTrue(v1.precedes(v2), errorMessage);
        Assertions.assertTrue(v1.precedes(v3), errorMessage);
        Assertions.assertTrue(v2.precedes(v3), errorMessage);
        Assertions.assertTrue(v1.precedes(v1), errorMessage);
        Assertions.assertFalse(v2.precedes(v1), errorMessage);
        Assertions.assertFalse(v1.precedes(v4), errorMessage);
        Assertions.assertFalse(v4.precedes(v1), errorMessage);
    }

    @Test
    public void followsTest(){
        Vector2d v1 = new Vector2d(1,1);
        Vector2d v2 = new Vector2d(2,2);
        Vector2d v3 = new Vector2d(2,3);
        Vector2d v4 = new Vector2d(0,3);

        Assertions.assertTrue(v3.follows(v2));
        Assertions.assertTrue(v3.follows(v1));
        Assertions.assertTrue(v2.follows(v1));
        Assertions.assertTrue(v1.follows(v1));
        Assertions.assertTrue(v2.follows(v2));
        Assertions.assertTrue(v3.follows(v3));
        Assertions.assertFalse(v1.follows(v3));
        Assertions.assertFalse(v1.follows(v4));
        Assertions.assertFalse(v4.follows(v1));
    }

    @Test
    public void upperRightTest(){
        Vector2d v1 = new Vector2d(1,1);
        Vector2d v2 = new Vector2d(2,2);
        Vector2d v3 = new Vector2d(2,3);

        Assertions.assertEquals(v1.upperRight(v2), new Vector2d(2,2), errorMessage);
        Assertions.assertEquals(v3.upperRight(v1), new Vector2d(2,3), errorMessage);
        Assertions.assertEquals(v2.upperRight(v1), new Vector2d(2,2), errorMessage);
    }

    @Test
    public void lowerLeftTest(){
        Vector2d v1 = new Vector2d(1,1);
        Vector2d v2 = new Vector2d(2,2);
        Vector2d v3 = new Vector2d(2,3);

        Assertions.assertEquals(v1.lowerLeft(v2), new Vector2d(1,1), errorMessage);
        Assertions.assertEquals(v3.lowerLeft(v1), new Vector2d(1,1), errorMessage);
        Assertions.assertEquals(v2.lowerLeft(v1), new Vector2d(1,1), errorMessage);
    }

    @Test
    public void add(){
        Vector2d v1 = new Vector2d(1,1);
        Vector2d v2 = new Vector2d(2,2);
        Vector2d v3 = new Vector2d(2,3);

        Assertions.assertEquals(v1.add(v2), new Vector2d(3,3), errorMessage);
        Assertions.assertEquals(v2.add(v3), new Vector2d(4,5), errorMessage);
        Assertions.assertEquals(v1.add(v3), new Vector2d(3,4), errorMessage);
        Assertions.assertEquals(v2.add(v1), new Vector2d(3,3), errorMessage);
    }

    @Test
    public void subtract(){
        Vector2d v1 = new Vector2d(1,1);
        Vector2d v2 = new Vector2d(2,2);
        Vector2d v3 = new Vector2d(2,3);

        Assertions.assertEquals(v1.subtract(v2), new Vector2d(-1,-1), errorMessage);
        Assertions.assertEquals(v2.subtract(v3), new Vector2d(0,-1), errorMessage);
        Assertions.assertEquals(v1.subtract(v3), new Vector2d(-1,-2), errorMessage);
        Assertions.assertEquals(v2.subtract(v1), new Vector2d(1,1), errorMessage);
        Assertions.assertEquals(v3.subtract(v1), new Vector2d(1,2), errorMessage);
        Assertions.assertEquals(v3.subtract(v2), new Vector2d(0,1), errorMessage);
    }

    @Test
    public void opposite(){
        Assertions.assertEquals(new Vector2d(1,1).opposite(), new Vector2d(-1,-1), errorMessage);
        Assertions.assertEquals(new Vector2d(-2,-2).opposite(), new Vector2d(2,2), errorMessage);
        Assertions.assertEquals(new Vector2d(2,-3).opposite(), new Vector2d(-2,3), errorMessage);
        Assertions.assertEquals(new Vector2d(-4,1).opposite(), new Vector2d(4,-1), errorMessage);
        Assertions.assertEquals(new Vector2d(0,0).opposite(), new Vector2d(0,0), errorMessage);

    }


}
