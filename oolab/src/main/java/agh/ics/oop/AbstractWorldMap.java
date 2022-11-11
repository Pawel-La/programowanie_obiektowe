package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractWorldMap implements IWorldMap {
    protected List<Animal> animals = new ArrayList<>();
    protected List<IMapElement> mapElements = new ArrayList<>();
    protected Vector2d leftLowerCorner;
    protected Vector2d rightUpperCorner;
    protected abstract Vector2d getDrawingLowerLeft();
    protected abstract Vector2d getDrawingUpperRight();

    @Override
    public String toString(){
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        System.out.println(getDrawingLowerLeft());
        System.out.println(getDrawingUpperRight());
        return mapVisualizer.draw(getDrawingLowerLeft(), getDrawingUpperRight());
    }
    protected Boolean isAnimalHere(Vector2d position) {
        for (Animal animal: animals)
            if (animal.getPosition().equals(position))
                return true;
        return false;
    }
    @Override
    public boolean canMoveTo(Vector2d position){
        return !isAnimalHere(position) &&
                position.follows(leftLowerCorner) &&
                position.precedes(rightUpperCorner);
    }
    @Override
    public boolean place(Animal animal){
        if (!canMoveTo(animal.getPosition()))
            return false;
        animals.add(animal);
        mapElements.add(animal);
        return true;
    }
    @Override
    public abstract boolean isOccupied(Vector2d position);
    protected Animal animalAt(Vector2d position){
        for (Animal animal: animals)
            if (animal.getPosition().equals(position))
                return animal;
        return null;
    }
    @Override
    public abstract Object objectAt(Vector2d position);
}
