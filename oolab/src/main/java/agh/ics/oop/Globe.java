package agh.ics.oop;

public class Globe implements IMapVariant{
    private final int mapWidth;
    private final int mapHeight;
    public Globe(int mapWidth, int mapHeight){
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }
    // if we get there it means that our animal tried to go beyond world bounds

    /**
     * Sets new position/orientation of animal
     * @param animal - animal that tried to move beyond world bounds
     * @param position - position out of bounds that animal tried to move to
     */
    @Override
    public void edgeService(Animal animal, Vector2d position) {
//        if animal tries to go through upper/lower bound of map turn it around
        if (position.y < 0 || position.y >= mapHeight)
            animal.turnAround();
//        else let it go to the other side of globe
        else
            animal.setPosition(new Vector2d((position.x + mapWidth) % mapWidth, position.y));
    }
}
