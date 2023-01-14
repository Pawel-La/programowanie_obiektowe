package agh.ics.oop; // za duży ten pakiet

import java.util.*;

/**
 * Abstract Class is responsible for randomizing process of growing grass
 */
abstract class AbstractGrassVariant implements IGrassVariant {
    Random random = new Random();  // modyfikator dostępu

    /**
     * Grows grass and returns positions of newly grown grasses
     *
     * @param numberOfGrasses       - number of grasses to grow
     * @param freePreferredSpots    - free spots that are preferred by grass
     * @param freeNotPreferredSpots - free spots that are not preferred by grass (2nd category spots)
     * @return positions of newly grown grasses
     */
    public Set<Vector2d> growGrass(int numberOfGrasses,
                                   Set<Vector2d> freePreferredSpots,
                                   Set<Vector2d> freeNotPreferredSpots) {
//        creates hashset for positions of new grasses
        Set<Vector2d> newGrasses = new HashSet<>();

        for (int i = 0; i < numberOfGrasses; i++) {
//            if there are no more free spots to grow, then the loop ends
            if (freePreferredSpots.size() == 0 && freeNotPreferredSpots.size() == 0)
                break;
            class Grow {
                void growAtPreferredSpot(Set<Vector2d> freePreferredSpots) {
//                creates list from set given to function
                    List<Vector2d> freePreferredSpotsAsArray = new ArrayList<>(freePreferredSpots);
//                choose random position
                    Vector2d position =
                            freePreferredSpotsAsArray.get(random.nextInt(freePreferredSpotsAsArray.size()));
//                deletes position from available positions
                    freePreferredSpots.remove(position);
//                add new grass position to result
                    newGrasses.add(position);
                }

                void growAtNonPreferredSpot(Set<Vector2d> freeNotPreferredSpots) {
//                creates list from set given to function
                    List<Vector2d> freeNotPreferredSpotsAsArray = new ArrayList<>(freeNotPreferredSpots);
//                choose random position
                    Vector2d position =
                            freeNotPreferredSpotsAsArray.get(random.nextInt(freeNotPreferredSpotsAsArray.size()));
//                deletes position from available positions
                    freeNotPreferredSpots.remove(position);
//                add new grass position to result
                    newGrasses.add(position);
                }
            }
//            if there is no more free preferred spots then grass grows at a random non-preferred spot
            if (freePreferredSpots.size() == 0)
                new Grow().growAtNonPreferredSpot(freeNotPreferredSpots);
//            if there is no more not preferred spots then grass grows at a random preferred spot
            else if (freeNotPreferredSpots.size() == 0)
                new Grow().growAtPreferredSpot(freePreferredSpots);
//            otherwise choose randomly (with 80:20 ratio) if grass should grow on preferred or non-preferred spot
            else {
                if (random.nextInt(5) != 0)
                    new Grow().growAtPreferredSpot(freePreferredSpots);
                else
                    new Grow().growAtNonPreferredSpot(freeNotPreferredSpots);
            }
        }
        return newGrasses;
    }

    public abstract void grassNoMoreAt(Vector2d position);

    public void deadAnimalAt(Vector2d position) {
    }
}
