package agh.ics.oop;

import agh.ics.oop.gui.App;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimulationEngine implements IEngine, Runnable{
    private final IWorldMap map;
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
        this.map = map;
        this.moveDelay = moveDelay;
        this.app = app;
        for (int i = 0; i < initNumberOfAnimals; i++){
            Animal animal = new Animal(map, energy, numberOfGenes, behaviorVariant);
            map.place(animal);
            animals.add(animal);
        }
    }
    private void moves(){
        for (Animal animal: animals) {
            animal.move();
            app.update();
            try {
                Thread.sleep(moveDelay);
            } catch (InterruptedException e) {
                System.out.println("przerwano prace");
            }
        }
    }
    public void clearDeadAnimals(){
        List<Animal> found = new ArrayList<>();
        for(Animal animal : animals){
            if(animal.dead()){
                found.add(animal);
                map.clearAnimal(animal, animal.getPosition());
            }
        }
        animals.removeAll(found);
    }

    @Override
    public void run() {
        for (int day = 0; day < 15; day++) {
            System.out.println("day: "+ (day+1));
            System.out.println("number of animals: "+animals.size());
            app.update();
            try{
                Thread.sleep(moveDelay);
            }catch (InterruptedException e){
                System.out.println("przerwano prace");
            }
            clearDeadAnimals();
            moves();
            animals.addAll(map.fightEatReproduce());
        }
    }
}
