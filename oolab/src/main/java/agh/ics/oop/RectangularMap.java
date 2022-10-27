package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class RectangularMap implements IWorldMap{
    final private Vector2d leftLowerCorner = new Vector2d(0,0);
    final private Vector2d rightUpperCorner;
    List<Animal> animals = new ArrayList<>();

    RectangularMap(int width, int height){
        rightUpperCorner = new Vector2d(width, height);
    }

    @Override
    public String toString(){
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw(leftLowerCorner, rightUpperCorner);
    }

    @Override
    public boolean canMoveTo(Vector2d position){
        return !isOccupied(position) &&
                position.follows(leftLowerCorner) &&
                position.precedes(rightUpperCorner);
    }

    @Override
    public boolean place(Animal animal){
        if (isOccupied(animal.getPosition())) {
            return false;
        }
        animals.add(animal);
        return true;
    }

    @Override
    public boolean isOccupied(Vector2d position){
        for (Animal animal: animals){
            if (animal.getPosition().equals(position)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Object objectAt(Vector2d position){
        if (!isOccupied(position)) return null;
        for (Animal animal: animals){
            if (animal.getPosition().equals(position)){
                return animal;
            }
        }
        return null;
    }
}
