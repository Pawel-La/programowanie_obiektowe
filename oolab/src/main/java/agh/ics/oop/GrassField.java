package agh.ics.oop;

import java.util.Random;

public class GrassField extends AbstractWorldMap implements IWorldMap{
    private final int grassEnergy;
    private final Random random = new Random();
    private final int mapWidth;
    private final int mapHeight;
    private final IMapVariant mapVariant;
    public GrassField(IMapVariant mapVariant, int numOfGrasses,
                      int grassEnergy, int mapWidth, int mapHeight){
        this.mapVariant = mapVariant;
        this.grassEnergy = grassEnergy;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        for (int i = 0; i < numOfGrasses; i++)
            addGrass();
        leftLowerCorner = new Vector2d(0,0);
        rightUpperCorner = new Vector2d(mapWidth - 1, mapHeight - 1);
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
    private void addGrass(){
        Vector2d position;
        do {
            position = getRandomPosition();
        }while (isOccupied(position));
        grasses.put(position, new Grass(position, grassEnergy));
    }

    public void eatGrass(Animal animal, Vector2d position){
        Grass grass = grasses.get(position);
        animal.addEnergy(grass.getEnergy());
        animal.addGrassesEaten();
        grasses.remove(position);
        addGrass();
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
}
