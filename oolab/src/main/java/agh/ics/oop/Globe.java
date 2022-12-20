package agh.ics.oop;

public class Globe implements IMapVariant{
    private final int mapWidth;
    private final int mapHeight;
    public Globe(int mapWidth, int mapHeight){
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }
    // if we get there it means that our animal tried to go beyond world bounds
    @Override
    public void edgeService(Animal animal, Vector2d position) {
        if (position.y < 0 || position.y >= mapHeight){
            animal.turnAround();
        }
        else{
            animal.setPosition(new Vector2d((position.x + mapWidth) % mapWidth, position.y));
        }
    }
}
