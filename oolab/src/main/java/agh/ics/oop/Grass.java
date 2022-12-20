package agh.ics.oop;

public class Grass implements IMapElement {
    private final Vector2d position;
    private final int energy;

    Grass(Vector2d position, int energy){
        this.position = position;
        this.energy = energy;
    }

    public Vector2d getPosition(){
        return position;
    }
    public int getEnergy(){
        return energy;
    }

    @Override
    public String getMapElementLookFile() {
        return "src/main/resources/grass.png";
    }

    public String toString(){
        return "TRAWA";
    }

}
