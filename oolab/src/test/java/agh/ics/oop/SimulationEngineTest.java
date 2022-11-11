package agh.ics.oop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimulationEngineTest {
    @Test
    public void runRectangularMapTest(){
        String arg = "f b r l f f r r f f f f f f f f";
        String [] args = arg.split("\\s+");
        MoveDirection[] directions = new OptionsParser().parse(args);
        IWorldMap map = new RectangularMap(10, 5);
        Vector2d[] positions = { new Vector2d(2,2), new Vector2d(3,4) };
        IEngine engine = new SimulationEngine(directions, map, positions);
        engine.run();

        Assertions.assertTrue(map.isOccupied(new Vector2d(3,4)));
        Assertions.assertTrue(map.isOccupied(new Vector2d(2,0)));
        Assertions.assertFalse(map.isOccupied(new Vector2d(4,3)));
        Assertions.assertEquals(map.objectAt(new Vector2d(3, 4)).toString(), "N");
        Assertions.assertEquals(map.objectAt(new Vector2d(2, 0)).toString(), "S");
    }

    @Test
    public void runGrassFieldTest(){
        String arg = "f b r l f f r r f f f f f f f f";
        String [] args = arg.split("\\s+");
        MoveDirection[] directions = new OptionsParser().parse(args);
        IWorldMap map = new GrassField(10);
        Vector2d[] positions = { new Vector2d(2,2), new Vector2d(3,4) };
        IEngine engine = new SimulationEngine(directions, map, positions);
        engine.run();

        Assertions.assertTrue(map.isOccupied(new Vector2d(2,-1)));
        Assertions.assertTrue(map.isOccupied(new Vector2d(3,7)));
        Assertions.assertEquals(map.objectAt(new Vector2d(2,-1)).toString(), "S");
        Assertions.assertEquals(map.objectAt(new Vector2d(3,7)).toString(), "N");
    }
}
