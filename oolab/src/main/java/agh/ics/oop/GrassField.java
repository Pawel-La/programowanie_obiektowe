package agh.ics.oop;

public class GrassField extends AbstractWorldMap implements IWorldMap{
    private final int num_of_grasses;
    GrassField(int num_of_grasses){
        this.num_of_grasses = num_of_grasses;
        for (int i = 0; i < num_of_grasses; i++)
            addGrass();
        leftLowerCorner = new Vector2d(Integer.MIN_VALUE,Integer.MIN_VALUE);
        rightUpperCorner = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    protected Vector2d getDrawingLowerLeft(){
        Vector2d lowerLeft = rightUpperCorner;
        for (IMapElement mapElement: mapElements)
            lowerLeft = mapElement.getPosition().lowerLeft(lowerLeft);
        return lowerLeft;
    }
    protected Vector2d getDrawingUpperRight(){
        Vector2d upperRight = leftLowerCorner;
        for (IMapElement mapElement: mapElements)
            upperRight = mapElement.getPosition().upperRight(upperRight);
        return upperRight;
    }
    private Vector2d getRandomPosition(){
        int x = (int) (Math.random() * Math.sqrt(10 * num_of_grasses));
        int y = (int) (Math.random() * Math.sqrt(10 * num_of_grasses));
        return new Vector2d(x,y);
    }
    private void addGrass(){
        Vector2d pos = getRandomPosition();
        while (isOccupied(pos)){
            pos = getRandomPosition();
        }
        mapElements.add(new Grass(pos));
    }
    private void eatGrass(Vector2d position){
        int size = mapElements.size();
        int idx = -1;
        for (int i = 0; i < size; i++){
            if (mapElements.get(i).getPosition().equals(position)){
                idx = i;
                break;
            }
        }
        mapElements.remove(idx);
        addGrass();
    }
    @Override
    public boolean canMoveTo(Vector2d position){
        boolean result = super.canMoveTo(position);
//        if result == true then there is no animal at the position
        if (result && isOccupied(position))
            eatGrass(position);
        return result;
    }
}
