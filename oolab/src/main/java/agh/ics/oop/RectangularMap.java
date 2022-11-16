package agh.ics.oop;

public class RectangularMap extends AbstractWorldMap implements IWorldMap{
    RectangularMap(int width, int height){
        leftLowerCorner = new Vector2d(0,0);
        rightUpperCorner = new Vector2d(width-1, height-1);
    }
    @Override
    public Vector2d getDrawingLowerLeft(){
        return leftLowerCorner;
    }
    @Override
    public Vector2d getDrawingUpperRight(){
        return rightUpperCorner;
    }
    @Override
    public Object objectAt(Vector2d position){
        for (IMapElement mapElement: mapElements)
            if (mapElement.getPosition().equals(position))
                return mapElement;
        return null;
    }
}
