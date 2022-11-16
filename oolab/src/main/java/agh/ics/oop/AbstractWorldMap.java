package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractWorldMap implements IWorldMap {
    protected List<IMapElement> mapElements = new ArrayList<>();
    protected Vector2d leftLowerCorner;
    protected Vector2d rightUpperCorner;
    protected abstract Vector2d getDrawingLowerLeft();
    protected abstract Vector2d getDrawingUpperRight();

    @Override
    public String toString(){
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw(getDrawingLowerLeft(), getDrawingUpperRight());
    }
    protected Boolean isAnimalHere(Vector2d position) {
        for (IMapElement mapElement: mapElements)
            if (mapElement instanceof Animal && mapElement.getPosition().equals(position))
                return true;
        return false;
    }
    @Override
    public boolean canMoveTo(Vector2d position){
        return position.follows(leftLowerCorner) &&
                position.precedes(rightUpperCorner) &&
                !isAnimalHere(position);
    }
    @Override
    public boolean place(Animal animal){
        if (!canMoveTo(animal.getPosition()))
            return false;
        mapElements.add(animal);
        return true;
    }
    @Override
    public boolean isOccupied(Vector2d position){
        for (IMapElement mapElement: mapElements)
            if (mapElement.getPosition().equals(position))
                return true;
        return false;
    }
    @Override
    public Object objectAt(Vector2d position){
        for (IMapElement mapElement: mapElements)
            if (mapElement.getPosition().equals(position))
                return mapElement;
        return null;
    }
}
