package agh.ics.oop;

import agh.ics.oop.gui.App;
import javafx.scene.layout.GridPane;

import java.io.FileNotFoundException;
import java.util.*;

public class SimulationEngine implements IEngine, Runnable{
    private final ToFileWriter toFileWriter = new ToFileWriter();
    private final IWorldMap map;
    private final App app;
    private final boolean saveToFile;
    private final GridPane gridPane;
    private final int moveDelay;
    private final List<Animal> animals = new ArrayList<>();
    private int numOfDeadAnimals = 0;
    private int totalAgeOfDeadAnimals = 0;
    public SimulationEngine (
            IWorldMap map,
            int initNumberOfAnimals,
            App app,
            boolean saveToFile,
            GridPane gridPane,
            int moveDelay,
            int energy,
            int numberOfGenes,
            IBehaviorVariant behaviorVariant){
        this.map = map;
        this.moveDelay = moveDelay;
        this.app = app;
        this.saveToFile = saveToFile;
        this.gridPane = gridPane;
        for (int i = 0; i < initNumberOfAnimals; i++){
            Animal animal = new Animal(map, energy, numberOfGenes, behaviorVariant);
            map.place(animal);
            animals.add(animal);
        }
        if (saveToFile){
            try {
                toFileWriter.setTitles();
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }
    private void moves(){
        for (Animal animal: animals) {
            animal.move();
            app.update(gridPane, map);
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
            if(animal.isDead()){
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
    private String getMostPopularGenes(){
        int [] genes;
        int [] mostPopularGenes = null;
        int numOfMostPopularGenes = 0;
        Map<int[], Integer> genomes = new HashMap<>();

        for (Animal animal: animals) {
            genes = animal.getGenes();
            if (genomes.get(genes) == null) {
                genomes.put(genes, 1);
                if (numOfMostPopularGenes == 0) {
                    mostPopularGenes = genes;
                    numOfMostPopularGenes = 1;
                }
            } else {
                genomes.put(genes, genomes.get(genes) + 1);
                if (genomes.get(genes) > numOfMostPopularGenes) {
                    numOfMostPopularGenes = genomes.get(genes);
                    mostPopularGenes = genes;
                }
            }
        }

        if (mostPopularGenes == null)
            return null;

        String [] mostPopularGenesAsString = new String[mostPopularGenes.length];
        for (int i = 0; i < mostPopularGenes.length; i++){
            mostPopularGenesAsString[i] = Integer.toString(mostPopularGenes[i]);
        }
        return String.join("", mostPopularGenesAsString);
    }
    private void setDailyInfo(int day){
        toFileWriter.setDay(day);
        toFileWriter.setNumOfAnimals(animals.size());
        toFileWriter.setNumOfGrasses(map.getNumOfGrasses());
        toFileWriter.setNumOfFreeSpots(map.getNumOfFreeSpots());
        toFileWriter.setMostPopularGenes(getMostPopularGenes());
        toFileWriter.setAverageEnergyForLivingAnimals(
                (animals.size() != 0) ? (getAnimalsTotalEnergy() / animals.size()) : 0);
        toFileWriter.setAverageAgeOfDeath(
                (numOfDeadAnimals != 0) ? (totalAgeOfDeadAnimals / numOfDeadAnimals) : 0);
    }
    private void getInfoAboutAnimal(Animal animal){
        System.out.println(Arrays.toString(animal.getGenes()));
        System.out.println(animal.getActiveGene());
        System.out.println(animal.getEnergy());
        System.out.println(animal.getEatenGrasses());
        System.out.println(animal.getChildren());
        System.out.println(animal.getAge());
        System.out.println(animal.isDead());
    }
    @Override
    public void run(){
        app.update(gridPane, map);
        for (int day = 0; day < 150; day++) {
            System.out.println("day: "+ (day+1));
            if (saveToFile){
                setDailyInfo(day+1);
                try {
                    toFileWriter.saveDailyInfo();
                }
                catch (FileNotFoundException e){
                    e.printStackTrace();
                }
            }
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
