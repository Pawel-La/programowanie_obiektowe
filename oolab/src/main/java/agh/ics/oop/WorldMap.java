package agh.ics.oop;

import java.util.*;
import static java.lang.Math.round;

/**
 * Class is the main class responsible for the whole map structure, holds info about animals and grasses,
 * while also giving ability to use some useful functions.
 */
public class WorldMap implements IWorldMap, IPositionChangeObserver{
    private final Random random = new Random();
    private final Map<Vector2d, List<Animal>> animals = new HashMap<>();
//    animals - map that keeps track of animals' positions, keeps every position where at least animals are present
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
//    grasses - map that keeps track of grasses' positions, keep every position that has grass
    private final Vector2d leftLowerCorner = new Vector2d(0,0);
//    leftLowerCorner - left lower corner of the map
    private final Vector2d rightUpperCorner;
//    rightUpperCorner - right upper corner of the map based on width and height of the map
    private final int grassEnergy;
    private final int grassesDaily;
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
    private final Comparator<Animal> compareAnimals;
    /**
     *
     * @param mapVariant - variant of map (edges service)
     * @param initNumOfGrasses - initial number of grasses
     * @param grassEnergy - number of energy that grass gives to animal that eats it
     * @param grassesDaily - number of grasses that grows every day
     * @param mapWidth - map width
     * @param mapHeight - map height
     * @param childEnergy - energy of newborn animal
     * @param fedEnergy - energy that animal needs to be able to reproduce
     * @param minMutations - minimal number of mutations to newborn genes
     * @param maxMutations - maximal number of mutations to newborn genes
     * @param mutationVariant - variant of mutation
     * @param behaviorVariant - variant of behavior
     * @param grassVariant - variant of grass growth
     */
    public WorldMap(IMapVariant mapVariant, int initNumOfGrasses,
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
        rightUpperCorner = new Vector2d(mapWidth - 1, mapHeight - 1);
        compareAnimals = animalComparator();
//        adding new grasses at the start of simulation
        Set<Vector2d> newGrassesPositions = grassVariant.growGrass(initNumOfGrasses);
        for (Vector2d newGrassPosition: newGrassesPositions){
            grasses.put(newGrassPosition, new Grass(newGrassPosition));
        }
    }
    @Override
    public Vector2d getLowerLeftMapCorner(){
        return leftLowerCorner;
    }
    @Override
    public Vector2d getUpperRightMapCorner(){
        return rightUpperCorner;
    }
    @Override
    public String toString(){
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw(getLowerLeftMapCorner(), getUpperRightMapCorner());
    }
    @Override
    public void place(Animal animal) throws IllegalArgumentException{
//        if there is animal already at position then add new animal to list of animals at this position
//        else create new list at position and put new animal here
        animals.computeIfAbsent(animal.getPosition(), k -> new ArrayList<>()).add(animal);
        animal.addObserver(this);
    }
    @Override
    public boolean isOccupied(Vector2d position){
        return (animals.get(position) != null && animals.get(position).size() > 0) ||
                grasses.get(position) != null;
    }
    @Override
    public Object objectAt(Vector2d position){
        if (animals.get(position) == null || animals.get(position).size() == 0)
            return grasses.get(position);
        return (animals.get(position)).get(0);
    }
    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        clearAnimal(animal, oldPosition);
//        if there is animal already at position then add new animal to list of animals at this position
//        else create new list at position and put new animal here
        animals.computeIfAbsent(newPosition, k -> new ArrayList<>()).add(animal);
    }
    @Override
    public Vector2d getRandomPosition(){
        int x = random.nextInt(mapWidth);
        int y = random.nextInt(mapHeight);
        return new Vector2d(x,y);
    }
    @Override
    public void edgeService(Animal animal){
//        sets position to the possible position that animal want to get to
        Vector2d position = animal.getPosition().add(animal.getOrientation().toUnitVector());
        if (!inBounds(position))
            mapVariant.edgeService(animal, position);
    }
    @Override
    public boolean inBounds(Vector2d position){
        return position.follows(leftLowerCorner) &&
                position.precedes(rightUpperCorner);
    }
    @Override
    public List<Animal> fightEatReproduce(){
        List<Animal> newbornAnimals = new ArrayList<>();
//        for every position where animals are present
        for (Vector2d position: animals.keySet()){
//            we sort animals using built comparator
            List<Animal> animalList = animals.get(position);
            animalList.sort(compareAnimals);
//            eat - first animal is the one that can eat if grass is here
            Animal animal1 = animalList.get(0);
            eatGrass(animal1);
            if (animalList.size() >= 2){
//            reproduce - second animal is the one that can reproduce with first animal if both are fed
                Animal animal2 = animalList.get(1);
                Animal newAnimal = reproduce(animal1, animal2);
                if (newAnimal != null){
                    place(newAnimal);
                    newbornAnimals.add(newAnimal);
                }
            }
        }
        return newbornAnimals;
    }
    @Override
    public void growGrass(){
        Set<Vector2d> newGrassesPositions = grassVariant.growGrass(grassesDaily);
        for (Vector2d newGrassPosition: newGrassesPositions)
            grasses.put(newGrassPosition, new Grass(newGrassPosition));
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
//        if animal is dead and grassVariant is ToxicCorpses then update grassVariant dead animals
        if (animal.getEnergy() <= 0 && grassVariant instanceof ToxicCorpses)
            grassVariant.deadAnimalAt(position);
//        we search trough animal's position animals list for the animal itself
        for(Animal animal1: animals.get(position)){
            if(animal1.equals(animal)){
                animals.get(position).remove(animal1);
                break;
            }
        }
//        if there is no more animals at this position we delete the position key from animals hashmap
        if (animals.get(position).isEmpty())
            animals.remove(position);
    }

    /**
     * @return Comparator that compares animals by energy, age and number of children
     */
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

    /**
     * If grass at the position then remove grass. Also update animal's energy and number of grasses eaten.
     * @param animal - The strongest animal at the position
     */
    private void eatGrass(Animal animal){
        Vector2d position = animal.getPosition();
        if (grasses.get(position) == null)
            return;
        animal.addEnergy(grassEnergy);
        animal.addGrassesEaten();
        grasses.remove(position);
//        inform grass variant that there is no grass
        grassVariant.grassNoMoreAt(position);
    }

    /**
     * Mutates given genes
     * @param genes - animal's genes (genome)
     * @return mutated genes
     */
    private int [] mutateGenes(int [] genes){
//        randomly chooses number of mutations from (minMutations, maxMutations)
        int numberOfMutations = random.nextInt(maxMutations - minMutations) + minMutations;
//        creates pool that will contain indexes of genome possible (not mutated yet) to mutate
        List<Integer> pool = new ArrayList<>();
//        copies genes
        int [] copy = genes.clone();
//        add all indexes from 0 to genes.length-1 to pool
        for (int i = 0; i < genes.length; i++)
            pool.add(i);
//        shuffle pool to get random indexes
        Collections.shuffle(pool);
//        takes random indexes from pool and mutate certain genes using mutationVariant
        for (int i = 0; i < numberOfMutations; i++)
            copy[pool.get(i)] = mutationVariant.getMutatedGene(copy[pool.get(i)]);
        return copy;
    }

    /**
     * Allows two given animals to reproduce if they are fed enough
     * @param animal1 - the strongest animal at the position
     * @param animal2 - the 2nd strongest animal at the position
     * @return newborn animal or null if animals weren't fed enough
     */
    private Animal reproduce(Animal animal1, Animal animal2){
        int animal1Energy = animal1.getEnergy();
        int animal2Energy = animal2.getEnergy();
//        we know that first animal energy is higher or the same as second animal's energy
//        ,so if 2nd animal is fed then 1st is also fed, so we check only second animal energy level
        if (animal2Energy < fedEnergy)
            return null;

//        number of genes that will be taken from animal 1, and from animal 2
        int numberOfAnimal1Genes, numberOfAnimal2Genes;
        int [] animal1Genes = animal1.getGenes();
        int [] animal2Genes = animal2.getGenes();
        int numberOfGenes = animal1Genes.length;
//        new animal's genes
        int [] newGenes = new int[numberOfGenes];

//        parents losing some energy for child
        animal1.lowerEnergy(childEnergy / 2);
        animal2.lowerEnergy(childEnergy - (childEnergy / 2));
        animal1.addChildren();
        animal2.addChildren();
//        calculating how many genes will be from 1st and 2nd animal
        numberOfAnimal1Genes =
                (int) round((animal1Energy * numberOfGenes) / (double) (animal1Energy + animal2Energy));
        numberOfAnimal2Genes = numberOfGenes - numberOfAnimal1Genes;
//        1st case - stronger animal gives genes from the front
        if (random.nextInt(2) == 0){
            System.arraycopy(animal1Genes, 0, newGenes, 0, numberOfAnimal1Genes);
            System.arraycopy(animal2Genes, numberOfAnimal1Genes, newGenes, numberOfAnimal1Genes,
                    numberOfGenes - numberOfAnimal1Genes);
        }
//        2nd case - stronger animal gives genes from the back
        else {
            System.arraycopy(animal2Genes, 0, newGenes, 0, numberOfAnimal2Genes);
            System.arraycopy(animal1Genes, numberOfAnimal2Genes, newGenes, numberOfAnimal2Genes,
                    numberOfGenes - numberOfAnimal2Genes);
        }
        newGenes = mutateGenes(newGenes);
        return new Animal(this, childEnergy, numberOfGenes,
                behaviorVariant, animal1.getPosition(), newGenes);
    }
//    public Map<Vector2d, List<Animal>> getAnimals(){
//        return animals;
//    }
//    public Map<Vector2d, Grass> getGrasses(){
//        return grasses;
//    }
}
