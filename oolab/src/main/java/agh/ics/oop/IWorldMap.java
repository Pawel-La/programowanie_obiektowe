package agh.ics.oop;

import java.util.List;

public interface IWorldMap {
    /**
     * Place given animal on the map.
     * @param animal - the animal to be placed on the map.
     */
    void place(Animal animal);

    /**
     * Return true if given position on the map is occupied.
     * @param position - position to check.
     * @return true if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Return an object at a given position.
     * @param position - the position of the object.
     * @return Object or null if the position is not occupied.
     */
    Object objectAt(Vector2d position);

    /**
     * @return random position inside a map
     */
    Vector2d getRandomPosition();

    /**
     * checks if animal after move will be inside a map, if not then do an edge service
     * @param animal - animal to do edge service on
     */
    void edgeService(Animal animal);

    /**
     * return true if position is in bounds of map
     * @param position - position of animal
     * @return true if position in bounds of map
     */
    boolean inBounds(Vector2d position);

    /**
     * removes animal from map
     * @param animal - animal to be removed
     * @param position - position from where animal should be removed
     */
    void clearAnimal(Animal animal, Vector2d position);
    /**
     * for every position with animal/animals simulate process of eating and reproducing
     * @return Array list of newborn animals
     */
    List<Animal> fightEatReproduce();
    /**
     * grows grass
     */
    void growGrass();
    /**
     * @return number of free spots on the map
     */
    int getNumOfFreeSpots();
    /**
     * @return number of grasses on the map
     */
    int getNumOfGrasses();
    /**
     * @return lower left corner of the map
     */
    Vector2d getLowerLeftMapCorner();
    /**
     * @return upper right corner of the map
     */
    Vector2d getUpperRightMapCorner();
}
