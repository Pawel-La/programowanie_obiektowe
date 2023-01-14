package agh.ics.oop;

public interface IPositionChangeObserver {
    /**
     * updates info about animal position
     *
     * @param animal      - animal that changed position
     * @param oldPosition - old position
     * @param newPosition - new position
     */
    void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition);
}
