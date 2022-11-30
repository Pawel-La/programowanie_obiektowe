package agh.ics.oop;

import java.util.ArrayList;

public class Animal implements IMapElement {
    private final ArrayList<IPositionChangeObserver> observers = new ArrayList<>();
    private MapDirection orientation = MapDirection.NORTH;
    private Vector2d position;
    private final IWorldMap map;

    Animal(IWorldMap map) {  // czemu konstruktory nie są publiczne?
        this.map = map;
        this.position = new Vector2d(0, 0);
    }

    Animal(IWorldMap map, Vector2d initialPosition) {
        this.map = map;
        this.position = initialPosition;
    }

    @Override
    public String toString() {
        return switch (this.orientation) {
            case NORTH -> "N";
            case SOUTH -> "S";
            case WEST -> "W";
            case EAST -> "E";
        };
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public Vector2d getPosition() {
        return position;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public void move(MoveDirection direction) {
        Vector2d possible_position, old_position;

        switch (direction) {
            case FORWARD -> {
                possible_position = position.add(orientation.toUnitVector());
                if (map.canMoveTo(possible_position)) {
                    old_position = position;
                    position = possible_position;
                    positionChanged(old_position, possible_position);
                }
            }
            case BACKWARD -> {
                possible_position = position.subtract(orientation.toUnitVector());
                if (map.canMoveTo(possible_position)) {
                    old_position = position;
                    position = possible_position;
                    positionChanged(old_position, possible_position);
                }
            }
            case LEFT -> orientation = orientation.previous();
            case RIGHT -> orientation = orientation.next();
        }
    }

    public void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {    // to nie może być publiczne; a nawet nie może być inne niż prywatne
        for (IPositionChangeObserver observer : observers) {
            observer.positionChanged(oldPosition, newPosition);
        }
    }
}
