package agh.ics.oop;

import java.util.ArrayList;
import java.util.Random;

public class Animal implements IMapElement {
    Random rand = new Random();
    private final ArrayList<IPositionChangeObserver> observers = new ArrayList<>();
    private MapDirection orientation = MapDirection.NORTH;
    private Vector2d position;
    private final IWorldMap map;
    private final int numberOfGenes;
    private int activeGene;
    private final int [] genes;
    private IBehaviorVariant behaviorVariant;
    private int energy;
    private int eatenGrasses;
    private int children;
    private int liveTime;

//    first animals constructor
    public Animal(IWorldMap map,
                  int energy,
                  int numberOfGenes,
                  IBehaviorVariant behaviorVariant){
        this.map = map;
        this.energy = energy;
        this.numberOfGenes = numberOfGenes;
        this.behaviorVariant = behaviorVariant;
        position = map.getRandomPosition();
        orientation = orientation.randomMapDirection();
        eatenGrasses = 0;
        children = 0;
        liveTime = 0;

        genes = new int[numberOfGenes];
        for (int i = 0; i < numberOfGenes; i++)
            genes[i] = rand.nextInt(8);

        activeGene = rand.nextInt(numberOfGenes);
    }
    @Override
    public String toString(){
        return switch (this.orientation){
            case NORTH -> "N";
            case SOUTH -> "S";
            case WEST -> "W";
            case EAST -> "E";
            case NORTH_EAST -> "NE";
            case SOUTH_EAST -> "SE";
            case SOUTH_WEST -> "SW";
            case NORTH_WEST -> "NW";
        };
    }

    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }

    public Vector2d getPosition(){
        return position;
    }
    public void setPosition(Vector2d position) {
        this.position = position;
    }

    @Override
    public String getMapElementLookFile() {
        return "src/main/resources/up.png";
//        return switch (this.orientation){
//            case NORTH -> "src/main/resources/up.png";
//            case SOUTH -> "src/main/resources/down.png";
    }

    public MapDirection getOrientation(){
        return orientation;
    }
    public void turnAround() {
        orientation = orientation.turn(4);
    }

    public void move(){
        if (energy <= 0)
            throw new RuntimeException("animal can't move, it is dead");
        energy--;
        liveTime++;

        Vector2d possiblePosition, oldPosition;
        orientation = orientation.turn(genes[activeGene]);
        activeGene = behaviorVariant.updateActiveGene(activeGene, numberOfGenes);
        oldPosition = position;
        possiblePosition = position.add(orientation.toUnitVector());

        if (map.inBounds(possiblePosition)){
            position = possiblePosition;
        }
        else {
            map.edgeService(this);
        }
        positionChanged(oldPosition, position);
    }
    public void addEnergy(int x){
        energy += x;
    }
    public void lowerEnergy(int x){
        energy -= x;
    }
    public void addGrassesEaten(){
        eatenGrasses++;
    }
    public void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }
    public void removeObserver(IPositionChangeObserver observer){
        observers.remove(observer);
    }
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for(IPositionChangeObserver observer : observers){
            observer.positionChanged(this, oldPosition, newPosition);
        }
    }
}
