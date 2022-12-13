package agh.ics.oop;

import agh.ics.oop.gui.App;
import java.util.ArrayList;
import java.util.List;

public class SimulationEngine implements IEngine, Runnable{
    private MoveDirection [] directions;
    private final App app;
    private final int moveDelay;
    private final List<Animal> animals = new ArrayList<>();
    public SimulationEngine (
                            IWorldMap map,
                            Vector2d [] positions,
                            App app,
                            int moveDelay){
        this.moveDelay = moveDelay;
        this.app = app;
        for (Vector2d position: positions){
            if (map.canMoveTo(position)) {
                Animal animal = new Animal(map, position);
                if (map.place(animal)) {
                    animal.addObserver(app);
                    app.addNewAnimal(animal);
                    animals.add(animal);
                }
            }
        }
    }
    @Override
    public void run() {
        int animal_num = 0;
        app.update();
        try{
            Thread.sleep(moveDelay);
        }catch (InterruptedException e){
            System.out.println("przerwano prace");
        }
        for (MoveDirection direction : directions){
            animals.get(animal_num).move(direction);
            animal_num++;
            animal_num %= animals.size();
            app.update();
            try{
                Thread.sleep(moveDelay);
            }catch (InterruptedException e){
                System.out.println("przerwano prace");
            }
        }
    }

    public void setDirections(MoveDirection [] directions){
        this.directions = directions;
    }
}
