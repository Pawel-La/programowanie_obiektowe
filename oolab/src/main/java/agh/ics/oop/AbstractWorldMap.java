package agh.ics.oop;

import java.util.*;

abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver{
    protected Map<Vector2d, List<Animal>> animals = new HashMap<>();
    protected Map<Vector2d, Grass> grasses = new HashMap<>();
    protected Vector2d leftLowerCorner;
    protected Vector2d rightUpperCorner;
    public abstract Vector2d getDrawingLowerLeft();
    public abstract Vector2d getDrawingUpperRight();
    public abstract void eatGrass(Animal animal, Vector2d position);
    @Override
    public String toString(){
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw(getDrawingLowerLeft(), getDrawingUpperRight());
    }
    @Override
    public boolean place(Animal animal) throws IllegalArgumentException{
        animals.computeIfAbsent(animal.getPosition(), k -> new ArrayList<>()).add(animal);
        animal.addObserver(this);
        return true;
    }
    @Override
    public boolean isOccupied(Vector2d position){
        return (animals.get(position) != null && animals.get(position).size() > 0) ||
                grasses.get(position) != null;
    }
    @Override
    public Object objectAt(Vector2d position){
        if (animals.get(position) == null || animals.get(position).size() == 0){
            return grasses.get(position);
        }
        return (animals.get(position)).get(0);
    }
    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        clearAnimal(animal, oldPosition);
        animals.computeIfAbsent(newPosition, k -> new ArrayList<>()).add(animal);
    }
    @Override
    public void clearAnimal(Animal animal, Vector2d position){
        Animal found = null;
        for(Animal animal1: animals.get(position)){
            if(animal1.equals(animal)){
                found = animal1;
                break;
            }
        }
        animals.get(position).remove(found);
        if (animals.get(position).isEmpty()) {
            animals.remove(position);
        }
    }
}
