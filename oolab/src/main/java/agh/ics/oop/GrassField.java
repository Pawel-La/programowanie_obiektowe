package agh.ics.oop;

public class GrassField extends AbstractWorldMap implements IWorldMap{
    private final int num_of_grasses;
    public GrassField(int num_of_grasses){
        this.num_of_grasses = num_of_grasses;
        for (int i = 0; i < num_of_grasses; i++)
            addGrass();
        leftLowerCorner = new Vector2d(Integer.MIN_VALUE,Integer.MIN_VALUE);
        rightUpperCorner = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    public Vector2d getDrawingLowerLeft(){
        return mapBoundary.getLeftLowerCorner();
    }
    public Vector2d getDrawingUpperRight(){
        return mapBoundary.getRightUpperCorner();
    }
    private Vector2d getRandomPosition(){
        int x = (int) (Math.random() * Math.sqrt(10 * num_of_grasses));
        int y = (int) (Math.random() * Math.sqrt(10 * num_of_grasses));
        return new Vector2d(x,y);
    }
    private void addGrass(){
        Vector2d position;
        do {
            position = getRandomPosition();
        }while (isOccupied(position));
        mapElements.put(position, new Grass(position));
        mapBoundary.addNewMapElement(position);
    }
    private void eatGrass(Vector2d position){
        mapElements.remove(position);
        mapBoundary.removeMapElement(position);
        addGrass();
    }
    @Override
    public boolean canMoveTo(Vector2d position){
        boolean result = super.canMoveTo(position);
//        if there is no animal yet sth is there then there is grass
        if (result && isOccupied(position))
            eatGrass(position);
        return result;
    }
}
