package agh.ics.oop;

import java.util.Random;

public class HellPortal implements IMapVariant{
    private final int mapWidth;
    private final int mapHeight;
    private final int childEnergy;
    public HellPortal(int mapWidth, int mapHeight, int childEnergy){
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.childEnergy = childEnergy;
    }
    public Vector2d getRandomPosition(){
        Random random = new Random();
        int x = random.nextInt(mapWidth);
        int y = random.nextInt(mapHeight);
        return new Vector2d(x,y);
    }
    // if we get there it means that our animal tried to go beyond world bounds
    @Override
    public void edgeService(Animal animal, Vector2d position) {
        Vector2d newPosition = position;
        while (newPosition == position)
            newPosition = getRandomPosition();
        animal.setPosition(newPosition);
        animal.lowerEnergy(childEnergy);
    }
}
