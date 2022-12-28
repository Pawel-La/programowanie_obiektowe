package agh.ics.oop;

import java.util.*;

public class ToxicCorpses extends AbstractGrassVariant{
    private final int mapWidth;
    private final int mapHeight;
    private final int numOfPreferred;
    int [][] deadAnimalsSpots;
    boolean [] isFreeSpots;
    protected Set<Vector2d> freePreferredSpots = new HashSet<>();
    protected Set<Vector2d> freeNotPreferredSpots = new HashSet<>();
    public ToxicCorpses(int mapWidth, int mapHeight){
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.numOfPreferred = mapWidth * mapHeight * 20/100;

        isFreeSpots = new boolean[mapWidth * mapHeight];
        for (int i = 0; i < mapWidth * mapHeight; i++)
            isFreeSpots[i] = true;

        deadAnimalsSpots = new int[mapWidth * mapHeight][2];
        for (int i = 0; i < mapWidth * mapHeight; i++) {
            deadAnimalsSpots[i][0] = 0;
            deadAnimalsSpots[i][1] = i;
        }
    }

    private Vector2d arrayIndexToPosition(int index){
        return new Vector2d(index / mapWidth, index % mapWidth);
    }
    private int positionToArrayIndex(Vector2d position){
        return position.x * mapWidth + position.y;
    }
    private static void shuffleArray(int[][] array)
    {
        int index;
        int [] temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    @Override
    public Set<Vector2d> growGrass(int numberOfGrasses) {
        int [][] deadAnimalsSpotsCopy = new int[deadAnimalsSpots.length][];
        for(int i = 0; i < deadAnimalsSpots.length; i++)
            deadAnimalsSpotsCopy[i] = deadAnimalsSpots[i].clone();
        shuffleArray(deadAnimalsSpotsCopy);

        java.util.Arrays.sort(deadAnimalsSpotsCopy, Comparator.comparingInt(a -> -a[0]));
        freePreferredSpots.clear();
        freeNotPreferredSpots.clear();

        for (int i = 0; i < numOfPreferred; i++){
            if (isFreeSpots[deadAnimalsSpotsCopy[i][1]]){
                freePreferredSpots.add(arrayIndexToPosition(deadAnimalsSpotsCopy[i][1]));
            }
        }
        for (int i = numOfPreferred; i < mapWidth * mapHeight; i++){
            if (isFreeSpots[deadAnimalsSpotsCopy[i][1]]){
                freeNotPreferredSpots.add(arrayIndexToPosition(deadAnimalsSpotsCopy[i][1]));
            }
        }

        return super.growGrass(numberOfGrasses, freePreferredSpots, freeNotPreferredSpots);
    }
    @Override
    public void grassNoMoreAt(Vector2d position) {
        isFreeSpots[positionToArrayIndex(position)] = true;
    }
    @Override
    public void deadAnimalAt(Vector2d position) {
        deadAnimalsSpots[positionToArrayIndex(position)][0]++;
    }
}
