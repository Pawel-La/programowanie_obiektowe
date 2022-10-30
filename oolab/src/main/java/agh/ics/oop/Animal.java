package agh.ics.oop;

public class Animal {
    private MapDirection orientation = MapDirection.NORTH;
    private Vector2d position;
    private IWorldMap map;

    Animal(){
    }
    Animal(IWorldMap map){
        this.map = map;
    }
    Animal(IWorldMap map, Vector2d initialPosition){
        this.map = map;
        this.position = initialPosition;
    }
    @Override
    public String toString(){
        return switch (this.orientation){
            case NORTH -> "N";
            case SOUTH -> "S";
            case WEST -> "W";
            case EAST -> "E";
        };
    }

    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }

    public Vector2d getPosition(){
        return position;
    }
    public MapDirection getOrientation(){
        return orientation;
    }

    public void move(MoveDirection direction){
        Vector2d possible_position;

        switch (direction){
            case FORWARD -> {
                possible_position = position.add(orientation.toUnitVector());
                if (map.canMoveTo(possible_position))
                    position = possible_position;
            }
            case BACKWARD -> {
                possible_position = position.subtract(orientation.toUnitVector());
                if (map.canMoveTo(possible_position))
                    position = possible_position;
            }
            case LEFT -> orientation = orientation.previous();
            case RIGHT -> orientation = orientation.next();
        }
    }
}
