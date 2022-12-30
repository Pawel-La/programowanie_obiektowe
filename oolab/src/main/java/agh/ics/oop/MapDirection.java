package agh.ics.oop;

import java.util.Random;

enum MapDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    /**
     * @return name of map direction as string
     */
    @Override
    public String toString(){
        return switch (this) {
            case NORTH      -> "Północ";
            case NORTH_EAST -> "Północny wschód";
            case EAST       -> "Wschód";
            case SOUTH_EAST -> "Południowy wschód";
            case SOUTH      -> "Południe";
            case SOUTH_WEST -> "Południowy zachód";
            case WEST       -> "Zachód";
            case NORTH_WEST -> "Północny zachód";
        };
    }

    /**
     * change int value into map direction
     * @param x - integer number from range 0 -> 7
     * @return map direction associated with given integer
     */
    private static MapDirection fromInt(int x){
        return switch (x) {
            case 0 -> NORTH;
            case 1 -> NORTH_EAST;
            case 2 -> EAST;
            case 3 -> SOUTH_EAST;
            case 4 -> SOUTH;
            case 5 -> SOUTH_WEST;
            case 6 -> WEST;
            case 7 -> NORTH_WEST;
            default -> throw new IllegalStateException("Unexpected value: " + x);
        };
    }

    /**
     * Turns this map direction into integer value
     * @return integer value associated with this map direction
     */
    private int toInt(){
        return switch (this) {
            case NORTH      -> 0;
            case NORTH_EAST -> 1;
            case EAST       -> 2;
            case SOUTH_EAST -> 3;
            case SOUTH      -> 4;
            case SOUTH_WEST -> 5;
            case WEST       -> 6;
            case NORTH_WEST -> 7;
        };
    }

    /**
     * @param x - integer from 0 -> 7
     * @return 'opposite' to x integer from 0 -> 7
     */
    public MapDirection turn(int x){
        return fromInt((toInt() + x) % 8);
    }

    /**
     * @return random map direction
     */
    public static MapDirection randomMapDirection(){
        Random rand = new Random();
        return fromInt(rand.nextInt(8));
    }

    /**
     * @return this map direction as a Vector2D
     */
    public Vector2d toUnitVector(){
        return switch (this) {
            case NORTH      ->  new Vector2d(0,1);
            case NORTH_EAST ->  new Vector2d(1,1);
            case EAST       ->  new Vector2d(1,0);
            case SOUTH_EAST ->  new Vector2d(1,-1);
            case SOUTH      ->  new Vector2d(0,-1);
            case SOUTH_WEST ->  new Vector2d(-1,-1);
            case WEST       ->  new Vector2d(-1,0);
            case NORTH_WEST ->  new Vector2d(-1,1);
        };
    }
}