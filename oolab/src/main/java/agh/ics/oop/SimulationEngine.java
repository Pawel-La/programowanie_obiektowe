package agh.ics.oop;

import agh.ics.oop.gui.App;
import java.util.*;

public class SimulationEngine implements IEngine, Runnable{
    private final IWorldMap map;
    private final App app;
    private final int moveDelay;
    private final List<Animal> animals = new ArrayList<>();
    int [] mostPopularGenes;
    private int numOfDeadAnimals = 0;
    private int totalAgeOfDeadAnimals = 0;
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
    private void clearDeadAnimals(){
        List<Animal> found = new ArrayList<>();
        for(Animal animal : animals){
            if(animal.dead()){
                numOfDeadAnimals++;
                totalAgeOfDeadAnimals += animal.getAge();
                found.add(animal);
                map.clearAnimal(animal, animal.getPosition());
            }
        }
        animals.removeAll(found);
    }
    private int getAnimalsTotalEnergy(){
        int totalEnergy = 0;
        for (Animal animal: animals){
            totalEnergy += animal.getEnergy();
        }
        return totalEnergy;
    }
    private int [] getMostPopularGenes(){
        int [] genes;
        int [] mostPopularGenes = null;
        int numOfMostPopularGenes = 0;
        Map<int[], Integer> genomes = new HashMap<>();

        for (Animal animal: animals){
            genes = animal.getGenes();
            if (genomes.get(genes) == null){
                genomes.put(genes, 1);
                if (numOfMostPopularGenes == 0){
                    mostPopularGenes = genes;
                    numOfMostPopularGenes = 1;
                }
            }
            else {
                genomes.put(genes, genomes.get(genes) + 1);
                if (genomes.get(genes) > numOfMostPopularGenes){
                    numOfMostPopularGenes = genomes.get(genes);
                    mostPopularGenes = genes;
                }
            }
        }

        return mostPopularGenes;
    }
    private void getInfoAboutSituation(){
        int numOfAnimals = animals.size();
        int numOfGrasses = map.getNumOfGrasses();
        int numOfFreeSpots = map.getNumOfFreeSpots();
        mostPopularGenes = getMostPopularGenes();
        int averageEnergyForLivingAnimals = (numOfAnimals != 0) ? (getAnimalsTotalEnergy() / numOfAnimals) : 0;
        int averageAgeOfDeath = (numOfDeadAnimals != 0) ? (totalAgeOfDeadAnimals / numOfDeadAnimals) : 0;
        System.out.println(numOfAnimals);
        System.out.println(numOfGrasses);
        System.out.println(numOfFreeSpots);
        System.out.println(Arrays.toString(mostPopularGenes));
        System.out.println(averageEnergyForLivingAnimals);
        System.out.println(averageAgeOfDeath);
    }
    @Override
    public void run() {
        for (int day = 0; day < 150; day++) {
            System.out.println("day: "+ (day+1));
            getInfoAboutSituation();
            app.update();
            try{
                Thread.sleep(moveDelay);
            }catch (InterruptedException e){
                System.out.println("przerwano prace");
            }
            clearDeadAnimals();
            moves();
            animals.addAll(map.fightEatReproduce());
            map.growGrass();
        }
    }
}
