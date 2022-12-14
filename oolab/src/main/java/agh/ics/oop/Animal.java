package agh.ics.oop;

import java.util.ArrayList;
import java.util.Random;

public class Animal implements IMapElement {
    Random rand = new Random();
    private final ArrayList<IPositionChangeObserver> observers = new ArrayList<>();
    private MapDirection orientation = MapDirection.NORTH;
    private Vector2d position;
    private final IWorldMap map;
    private final int number_of_genomes;
    private int activeGenome;
    private final int [] genomes;
    private IBehaviorVariant behaviorVariant;
    private int energy;
    private int eatenPlants;
    private int children;
    private int liveTime;

//    first animals constructor
    public Animal(IWorldMap map,
                  Vector2d initialPosition,
                  int energy,
                  int number_of_genomes,
                  IBehaviorVariant behaviorVariant){
        this.map = map;
        this.position = initialPosition;
        this.energy = energy;
        this.number_of_genomes = number_of_genomes;
        this.behaviorVariant = behaviorVariant;
        orientation = orientation.randomMapDirection();
        eatenPlants = 0;
        children = 0;
        liveTime = 0;

        genomes = new int[number_of_genomes];
        for (int i = 0; i < number_of_genomes; i++)
            genomes[i] = rand.nextInt(8);

        activeGenome = rand.nextInt(number_of_genomes);
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

    @Override
    public String getMapElementLookFile() {
        return "src/main/resources/up.png";
//        return switch (this.orientation){
//            case NORTH -> "src/main/resources/up.png";
//            case SOUTH -> "src/main/resources/down.png";
//            case WEST -> "src/main/resources/left.png";
//            case EAST -> "src/main/resources/right.png";
//            case NORTH_EAST -> "src/main/resources/up.png";
//            case SOUTH_EAST -> "src/main/resources/up.png";
//            case SOUTH_WEST -> "src/main/resources/up.png";
//            case NORTH_WEST -> "src/main/resources/up.png";
//        };
    }

    public MapDirection getOrientation(){
        return orientation;
    }

    public void move(){
        if (energy <= 0)
            throw new RuntimeException("animal can't move, it is dead");
        energy--;
        liveTime++;

        Vector2d possible_position, old_position;
        orientation = orientation.turn(genomes[activeGenome]);
        activeGenome = behaviorVariant.updateActiveGenome(activeGenome, number_of_genomes);

        possible_position = position.add(orientation.toUnitVector());
        if (map.canMoveTo(possible_position)) {
            old_position = position;
            position = possible_position;
            positionChanged(old_position, possible_position);
        }
    }
    public void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }
    public void removeObserver(IPositionChangeObserver observer){
        observers.remove(observer);
    }
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for(IPositionChangeObserver observer : observers){
            observer.positionChanged(oldPosition, newPosition);
        }
    }
}
