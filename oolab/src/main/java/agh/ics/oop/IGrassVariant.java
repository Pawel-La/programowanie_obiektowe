package agh.ics.oop;

import java.util.Set;

public interface IGrassVariant {
    Set<Vector2d> growGrass(int numberOfGrasses);
    void grassNoMoreAt(Vector2d position);
}
