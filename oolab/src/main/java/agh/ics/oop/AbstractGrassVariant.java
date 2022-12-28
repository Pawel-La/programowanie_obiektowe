package agh.ics.oop;

import java.util.*;

abstract class AbstractGrassVariant implements IGrassVariant{
    Random random = new Random();
    public Set<Vector2d> growGrass(int numberOfGrasses,
                                   Set<Vector2d> freePreferredSpots,
                                   Set<Vector2d> freeNotPreferredSpots) {
        Set<Vector2d> newGrasses = new HashSet<>();
        Vector2d position;

        for (int i = 0; i < numberOfGrasses; i++){
            if (freePreferredSpots.size() == 0 && freeNotPreferredSpots.size() == 0)
                break;
            List<Vector2d> freePreferredSpotsAsArray;
            List<Vector2d> freeNotPreferredSpotsAsArray;
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
}
