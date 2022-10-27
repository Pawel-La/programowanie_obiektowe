package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine implements IEngine{
    private final MoveDirection [] directions;
    List<Animal> animals = new ArrayList<>();
    SimulationEngine(MoveDirection [] directions, IWorldMap map, Vector2d [] positions){
        this.directions = directions;
        for (Vector2d position: positions){
            if (map.canMoveTo(position)) {
                Animal animal = new Animal(map, position);
                map.place(animal);
                animals.add(animal);
            }
        }
    }

    @Override
    public void run() {
        int animal_num = 0;
        for (MoveDirection direction : directions){
            animals.get(animal_num).move(direction);
            animal_num ++;
            animal_num %= animals.size();
        }
    }
}
