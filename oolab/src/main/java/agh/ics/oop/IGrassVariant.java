package agh.ics.oop;

import java.util.Set;

public interface IGrassVariant {
    /**
     * Grows given number of grasses
     * @param numberOfGrasses - number of grasses to grow
     * @return set of positions of new grasses
     */
    Set<Vector2d> growGrass(int numberOfGrasses);

    /**
     * Informs about grass position
     * @param position - position that grass is no more at
     */
    void grassNoMoreAt(Vector2d position);

    /**
     * Informs about where animal died
     * @param position - position that animal died at
     */
    void deadAnimalAt(Vector2d position);
}
