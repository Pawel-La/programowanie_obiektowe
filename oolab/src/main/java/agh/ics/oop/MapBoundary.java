package agh.ics.oop;

import java.util.SortedSet;
import java.util.TreeSet;

public class MapBoundary implements IPositionChangeObserver {
    private final SortedSet<Vector2d> elementsPositionsX = new TreeSet<>((o1, o2) -> {
        if (o1.x > o2.x || (o1.x == o2.x && o1.y > o2.y))
            return 1;
        if (o1.x < o2.x || o1.y < o2.y)
            return -1;
        return 0;
    });
    private final SortedSet<Vector2d> elementsPositionsY = new TreeSet<>((o1, o2) -> {
        if (o1.y > o2.y || (o1.y == o2.y && o1.x > o2.x))
            return 1;
        if (o1.y < o2.y || o1.x < o2.x)
            return -1;
        return 0;
    });
    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        removeMapElement(oldPosition);
        addNewMapElement(newPosition);
    }
    public void addNewMapElement(Vector2d elementPosition){
        elementsPositionsX.add(elementPosition);
        elementsPositionsY.add(elementPosition);
    }

    public void removeMapElement(Vector2d elementPosition){
        elementsPositionsX.remove(elementPosition);
        elementsPositionsY.remove(elementPosition);
    }

    public Vector2d getLeftLowerCorner(){
        return new Vector2d(elementsPositionsX.first().x, elementsPositionsY.first().y);
    }
    public Vector2d getRightUpperCorner(){
        return new Vector2d(elementsPositionsX.last().x, elementsPositionsY.last().y);
    }
}
