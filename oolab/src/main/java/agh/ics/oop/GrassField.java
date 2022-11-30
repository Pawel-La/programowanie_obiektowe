package agh.ics.oop;

public class GrassField extends AbstractWorldMap implements IWorldMap {
    private final int num_of_grasses;

    GrassField(int num_of_grasses) {
        this.num_of_grasses = num_of_grasses;
        for (int i = 0; i < num_of_grasses; i++)
            addGrass();
        leftLowerCorner = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
        rightUpperCorner = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    protected Vector2d getDrawingLowerLeft() {
        Vector2d lowerLeft = rightUpperCorner;
        for (Vector2d pos : mapElements.keySet())
            lowerLeft = pos.lowerLeft(lowerLeft);
        return lowerLeft;
    }

    protected Vector2d getDrawingUpperRight() {
        Vector2d upperRight = leftLowerCorner;
        for (Vector2d pos : mapElements.keySet())
            upperRight = pos.upperRight(upperRight);
        return upperRight;
    }

    private Vector2d getRandomPosition() {
        int x = (int) (Math.random() * Math.sqrt(10 * num_of_grasses));
        int y = (int) (Math.random() * Math.sqrt(10 * num_of_grasses));
        return new Vector2d(x, y);
    }

    private void addGrass() {
        Vector2d pos;
        do {
            pos = getRandomPosition();
        } while (isOccupied(pos));
        mapElements.put(pos, new Grass(pos));
    }

    private void eatGrass(Vector2d position) {
        mapElements.remove(position);
        addGrass();
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        boolean result = super.canMoveTo(position);
//        if there is no animal yet sth is there then there is grass
        if (result && isOccupied(position))
            eatGrass(position); // a dlaczego to nie jest przeniesione do positionChanged?
        return result;
    }
}
