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
        if (!isOccupied(pos)){
            mapElements.add(new Grass(pos));
        }
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
        while (mapElements.size() < size)
            addGrass();
    }
    @Override
    public boolean canMoveTo(Vector2d position){
        boolean result = !isAnimalHere(position) &&
                position.follows(leftLowerCorner) &&
                position.precedes(rightUpperCorner);
//        it means that there is a non-animal object at the position
        if (result && isOccupied(position))
            eatGrass(position);
        return result;
    }
    @Override
    public boolean isOccupied(Vector2d position) {
        for (IMapElement mapElement: mapElements)
            if (position.equals(mapElement.getPosition()))
                return true;
        return false;
    }
    private IMapElement mapElementAt(Vector2d position){
        for (IMapElement mapElement: mapElements)
            if (mapElement.getPosition().equals(position))
                return mapElement;
        return null;
    }
    @Override
    public Object objectAt(Vector2d position) {
        if (animalAt(position) != null)
            return animalAt(position);
        return mapElementAt(position);
    }
}
