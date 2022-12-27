package agh.ics.oop;

import java.util.*;

public class WoodedEquators implements IGrassVariant{
    Random random = new Random();
    int mapWidth, mapHeight;
    int topBound, bottomBound;
    int grassesDaily;
    protected Set<Vector2d> freePreferredSpots = new HashSet<>();
    protected Set<Vector2d> freeNotPreferredSpots = new HashSet<>();
    List<Vector2d> freePreferredSpotsAsArray;
    List<Vector2d> freeNotPreferredSpotsAsArray;

    public WoodedEquators(int mapWidth, int mapHeight, int grassesDaily){
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.grassesDaily = grassesDaily;
        topBound = mapHeight * 60/100;
        bottomBound = mapHeight * 40/100;
        for (int i = 0; i < mapWidth; i++){
            for (int j = 0; j < mapHeight; j++){
                if (bottomBound <= j && j <= topBound)
                    freePreferredSpots.add(new Vector2d(i,j));
                else
                    freeNotPreferredSpots.add(new Vector2d(i,j));
            }
        }
    }

    @Override
    public Set<Vector2d> growGrass(int numberOfGrasses) {
        Set<Vector2d> newGrasses = new HashSet<>();
        Vector2d position;

        for (int i = 0; i < numberOfGrasses; i++){
            if (freePreferredSpots.size() == 0 && freeNotPreferredSpots.size() == 0)
                break;
            if (freePreferredSpots.size() == 0){
                freeNotPreferredSpotsAsArray = new ArrayList<>(freeNotPreferredSpots);
                position = freeNotPreferredSpotsAsArray.get(random.nextInt(freeNotPreferredSpotsAsArray.size()));
                freeNotPreferredSpots.remove(position);
                newGrasses.add(position);
            }
            else if (freeNotPreferredSpots.size() == 0){
                freePreferredSpotsAsArray = new ArrayList<>(freePreferredSpots);
                position = freePreferredSpotsAsArray.get(random.nextInt(freePreferredSpotsAsArray.size()));
                freePreferredSpots.remove(position);
                newGrasses.add(position);
            }
            else{
                if (random.nextInt(5) != 0){
                    freePreferredSpotsAsArray = new ArrayList<>(freePreferredSpots);
                    position = freePreferredSpotsAsArray.get(random.nextInt(freePreferredSpotsAsArray.size()));
                    freePreferredSpots.remove(position);
                    newGrasses.add(position);
                }
                else {
                    freeNotPreferredSpotsAsArray = new ArrayList<>(freeNotPreferredSpots);
                    position = freeNotPreferredSpotsAsArray.get(random.nextInt(freeNotPreferredSpotsAsArray.size()));
                    freeNotPreferredSpots.remove(position);
                    newGrasses.add(position);
                }
            }
        }
        return newGrasses;
    }

    @Override
    public void grassNoMoreAt(Vector2d position) {
        if (bottomBound <= position.y && position.y <= topBound)
            freePreferredSpots.add(position);
        else
            freeNotPreferredSpots.add(position);
    }
}
