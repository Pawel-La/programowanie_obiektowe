package agh.ics.oop;

import agh.ics.oop.gui.App;
import java.util.ArrayList;
import java.util.List;

public class SimulationEngine implements IEngine, Runnable{
    private final App app;
    private final int moveDelay;
    private final List<Animal> animals = new ArrayList<>();

    public SimulationEngine (
            IWorldMap map,
            int initNumberOfAnimals,
            App app,
            int moveDelay,
            int energy,
            int numberOfGenes,
            IBehaviorVariant behaviorVariant){
        this.moveDelay = moveDelay;
        this.app = app;
        for (int i = 0; i < initNumberOfAnimals; i++){
            Animal animal = new Animal(map, energy, numberOfGenes, behaviorVariant);
            map.place(animal);
            animals.add(animal);
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
        for(int i = 0; i < 41; i++) {
            animals.get(animal_num).move();
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
}
