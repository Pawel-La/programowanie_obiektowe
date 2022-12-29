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

    /**
     * teleports animal to a random new position and take a bit of its health
     * @param animal - animal that entered hell portal
     * @param position - position beyond map that animal tried to go to
     */
    @Override
    public void edgeService(Animal animal, Vector2d position) {
        animal.setPosition(getRandomPosition());
        animal.lowerEnergy(childEnergy);
    }
}
