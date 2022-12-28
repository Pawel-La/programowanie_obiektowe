package agh.ics.oop;

import java.util.*;
import static java.lang.Math.round;

public class GrassField extends AbstractWorldMap implements IWorldMap{
    private final int grassEnergy;
    private final int grassesDaily;
    private final Random random = new Random();
    private final int mapWidth;
    private final int mapHeight;
    private final int childEnergy;
    private final int fedEnergy;
    private final int minMutations;
    private final int maxMutations;
    private final IMutationVariant mutationVariant;
    private final IBehaviorVariant behaviorVariant;
    private final IMapVariant mapVariant;
    private final IGrassVariant grassVariant;
    public GrassField(IMapVariant mapVariant, int initNumOfGrasses,
                      int grassEnergy, int grassesDaily, int mapWidth, int mapHeight,
                      int childEnergy, int fedEnergy, int minMutations,
                      int maxMutations, IMutationVariant mutationVariant,
                      IBehaviorVariant behaviorVariant, IGrassVariant grassVariant){
        this.mapVariant = mapVariant;
        this.grassEnergy = grassEnergy;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.childEnergy = childEnergy;
        this.fedEnergy = fedEnergy;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.mutationVariant = mutationVariant;
        this.behaviorVariant = behaviorVariant;
        this.grassVariant = grassVariant;
        this.grassesDaily = grassesDaily;
        leftLowerCorner = new Vector2d(0,0);
        rightUpperCorner = new Vector2d(mapWidth - 1, mapHeight - 1);
        System.out.println(grassVariant instanceof ToxicCorpses);

        Set<Vector2d> newGrassesPositions = grassVariant.growGrass(initNumOfGrasses);
        for (Vector2d newGrassPosition: newGrassesPositions){
            grasses.put(newGrassPosition, new Grass(newGrassPosition));
        }
    }
    public Vector2d getDrawingLowerLeft(){
        return leftLowerCorner;
    }
    public Vector2d getDrawingUpperRight(){
        return rightUpperCorner;
    }
    public Vector2d getRandomPosition(){
        int x = random.nextInt(mapWidth);
        int y = random.nextInt(mapHeight);
        return new Vector2d(x,y);
    }
    public void edgeService(Animal animal){
        Vector2d position = animal.getPosition().add(animal.getOrientation().toUnitVector());
        if (!(position.follows(leftLowerCorner) &&
                position.precedes(rightUpperCorner))){
            mapVariant.edgeService(animal, position);
        }
    }
    public boolean inBounds(Vector2d position){
        return position.follows(leftLowerCorner) &&
                position.precedes(rightUpperCorner);
    }
    private Comparator<Animal> animalComparator(){
        Comparator<Animal> compareByEnergy =
                Comparator.comparing(Animal::getEnergy, Comparator.reverseOrder());
        Comparator<Animal> compareByAge =
                Comparator.comparing(Animal::getAge, Comparator.reverseOrder());
        Comparator<Animal> compareByNumOfKids =
                Comparator.comparing(Animal::getChildren, Comparator.reverseOrder());
        return compareByEnergy
                .thenComparing(compareByAge)
                .thenComparing(compareByNumOfKids);
    }
    public void eatGrass(Animal animal, Vector2d position){
        animal.addEnergy(grassEnergy);
        animal.addGrassesEaten();
        grasses.remove(position);
        grassVariant.grassNoMoreAt(position);
    }
    private int [] mutateGenes(int [] genes){
        int numberOfMutations = random.nextInt(maxMutations - minMutations) + minMutations;
        List<Integer> pool = new ArrayList<>();
        int [] copy = genes.clone();
        for (int i = 0; i < genes.length; i++)
            pool.add(i);
        Collections.shuffle(pool);
        for (int i = 0; i < numberOfMutations; i++)
            copy[pool.get(i)] = mutationVariant.getMutatedGene(copy[pool.get(i)]);
        return copy;
    }
    private Animal reproduce(Animal animal1, Animal animal2){
        int animal1Energy = animal1.getEnergy();
        int animal2Energy = animal2.getEnergy();
//        we know that first animal energy is higher or the same as second animal's energy
//        ,so we check only second animal energy level
        if (animal2Energy < fedEnergy)
            return null;

        int numberOfAnimal1Genes, numberOfAnimal2Genes;
        int [] animal1Genes = animal1.getGenes();
        int [] animal2Genes = animal2.getGenes();
        int numberOfGenes = animal1Genes.length;
        int [] newGenes = new int[numberOfGenes];
        double d;

//        parents losing some energy for child
        animal1.lowerEnergy(childEnergy / 2);
        animal2.lowerEnergy(childEnergy - (childEnergy / 2));
        animal1.addChildren();
        animal2.addChildren();
//        calculating how many genes will be from 1st and 2nd animal
        d = (animal1Energy * numberOfGenes)/ (double) (animal1Energy + animal2Energy);
        numberOfAnimal1Genes = (int) round(d);
        numberOfAnimal2Genes = numberOfGenes - numberOfAnimal1Genes;
//        stronger animal gives genes from the front
        if (random.nextInt(2) == 0){
            System.arraycopy(animal1Genes, 0, newGenes, 0, numberOfAnimal1Genes);
            System.arraycopy(animal2Genes, numberOfAnimal1Genes, newGenes, numberOfAnimal1Genes,
                    numberOfGenes - numberOfAnimal1Genes);
        }
        //        stronger animal gives genes from the back
        else {
            System.arraycopy(animal2Genes, 0, newGenes, 0, numberOfAnimal2Genes);
            System.arraycopy(animal1Genes, numberOfAnimal2Genes, newGenes, numberOfAnimal2Genes,
                    numberOfGenes - numberOfAnimal2Genes);
        }
        newGenes = mutateGenes(newGenes);
        return new Animal(this, childEnergy, numberOfGenes,
                behaviorVariant, animal1.getPosition(), newGenes);
    }
    @Override
    public List<Animal> fightEatReproduce(){
        List<Animal> newAnimals = new ArrayList<>();
        for (Vector2d position: animals.keySet()){
            List<Animal> animalList = animals.get(position);
            Comparator<Animal> compareAnimals = animalComparator();
            animalList.sort(compareAnimals);
            Animal animal1 = animalList.get(0);
            if (grasses.get(position) != null){
                eatGrass(animal1, position);
            }
            if (animalList.size() >= 2){
                Animal animal2 = animalList.get(1);
                Animal newAnimal = reproduce(animal1, animal2);
                if (newAnimal != null){
                    place(newAnimal);
                    newAnimals.add(newAnimal);
                }
            }
        }
        return newAnimals;
    }
    @Override
    public void growGrass(){
        Set<Vector2d> newGrassesPositions = grassVariant.growGrass(grassesDaily);
        for (Vector2d newGrassPosition: newGrassesPositions){
            grasses.put(newGrassPosition, new Grass(newGrassPosition));
        }
    }
    @Override
    public int getNumOfFreeSpots(){
        int count = 0;
        for (int i = 0; i < mapWidth; i++){
            for (int j = 0; j < mapHeight; j++){
                if (!isOccupied(new Vector2d(i,j)))
                    count++;
            }
        }
        return count;
    }
    @Override
    public int getNumOfGrasses(){
        return grasses.size();
    }
    @Override
    public void clearAnimal(Animal animal, Vector2d position){
        if (animal.getEnergy() <= 0){
            grassVariant.deadAnimalAt(position);
        }
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
