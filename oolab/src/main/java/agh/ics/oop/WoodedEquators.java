package agh.ics.oop;

import java.util.HashSet;
import java.util.Set;

public class WoodedEquators extends AbstractGrassVariant implements IGrassVariant {
    int mapWidth, mapHeight;
    int topBound, bottomBound;
    protected Set<Vector2d> freePreferredSpots = new HashSet<>();
    protected Set<Vector2d> freeNotPreferredSpots = new HashSet<>();

    public WoodedEquators(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        topBound = mapHeight * 60 / 100;
        bottomBound = mapHeight * 40 / 100;
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                if (bottomBound <= j && j <= topBound)
                    freePreferredSpots.add(new Vector2d(i, j));
                else
                    freeNotPreferredSpots.add(new Vector2d(i, j));
            }
        }
    }

    @Override
    public void grassNoMoreAt(Vector2d position) {
        if (bottomBound <= position.y && position.y <= topBound)
            freePreferredSpots.add(position);
        else
            freeNotPreferredSpots.add(position);
    }

    @Override
    public Set<Vector2d> growGrass(int numberOfGrasses) {
        return super.growGrass(numberOfGrasses, freePreferredSpots, freeNotPreferredSpots);
    }
}
