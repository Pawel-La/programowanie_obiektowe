package agh.ics.oop;

import java.util.HashMap;
import java.util.Map;

abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected Map<Vector2d, IMapElement> mapElements = new HashMap<>();
    protected Vector2d leftLowerCorner;
    protected Vector2d rightUpperCorner;

    protected abstract Vector2d getDrawingLowerLeft();

    protected abstract Vector2d getDrawingUpperRight();

    private MapVisualizer mapVisualizer = new MapVisualizer(this);

    @Override
    public String toString() {
        return mapVisualizer.draw(getDrawingLowerLeft(), getDrawingUpperRight());
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(leftLowerCorner) &&
                position.precedes(rightUpperCorner) &&
                !(mapElements.get(position) instanceof Animal);
    }

    @Override
    public boolean place(Animal animal) {
        Vector2d position = animal.getPosition();
        if (!canMoveTo(position))
            return false;
        mapElements.put(position, animal);
        animal.addObserver(this);
        return true;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return mapElements.get(position) != null;
    }

    @Override
    public Object objectAt(Vector2d position) {
        return mapElements.get(position);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        IMapElement animal = mapElements.get(oldPosition);
        mapElements.remove(oldPosition);
        mapElements.put(newPosition, animal);
    }
}
