package agh.ics.oop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
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
        System.out.println("length: "+ animals.get(position).size());
        return (animals.get(position)).get(0);
    }
    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        if (animals.get(oldPosition) == null)
            throw new RuntimeException("cos poszlo nie tak w position changed");
        Animal found = null;
        for(Animal animal1: animals.get(oldPosition)){
            if(animal1.equals(animal)){
                found = animal1;
                break;
            }
        }
        animals.get(oldPosition).remove(found);
        animals.computeIfAbsent(newPosition, k -> new ArrayList<>()).add(animal);
    }
}
