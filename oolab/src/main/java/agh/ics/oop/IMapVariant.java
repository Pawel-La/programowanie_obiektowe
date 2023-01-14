package agh.ics.oop;

public interface IMapVariant {
    /**
     * Sets new position/orientation of animal
     *
     * @param animal   - animal that tried to move beyond world bounds
     * @param position - position out of bounds that animal tried to move to
     */
    void edgeService(Animal animal, Vector2d position);
}
