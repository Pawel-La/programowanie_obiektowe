package agh.ics.oop;

import java.util.HashMap;
import java.util.Map;

abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected MapBoundary mapBoundary = new MapBoundary();
    protected Map<Vector2d, IMapElement> mapElements = new HashMap<>();
    protected Vector2d leftLowerCorner;
    protected Vector2d rightUpperCorner;
    public abstract Vector2d getDrawingLowerLeft();
    public abstract Vector2d getDrawingUpperRight();
    @Override
    public String toString(){
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw(getDrawingLowerLeft(), getDrawingUpperRight());
    }
    @Override
    public boolean canMoveTo(Vector2d position){
        return position.follows(leftLowerCorner) &&
                position.precedes(rightUpperCorner) &&
                !(mapElements.get(position) instanceof Animal);
    }
    @Override
    public boolean place(Animal animal) throws IllegalArgumentException{
        Vector2d position = animal.getPosition();
        if (!canMoveTo(position))
            throw new IllegalArgumentException("not possible to add element to position: " + position +
                    " position already taken");
        mapElements.put(position, animal);
        mapBoundary.addNewMapElement(position);
        animal.addObserver(this);
        animal.addObserver(mapBoundary);
        return true;
    }
    @Override
    public boolean isOccupied(Vector2d position){
        return mapElements.get(position) != null;
    }
    @Override
    public Object objectAt(Vector2d position){
        return mapElements.get(position);
    }
    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        IMapElement animal = mapElements.get(oldPosition);
        mapElements.remove(oldPosition);
        mapElements.put(newPosition, animal);
    }
}
