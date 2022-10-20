package agh.ics.oop;


public class Animal {
    private MapDirection orientation = MapDirection.NORTH;
    private Vector2d position = new Vector2d(2,2);

    @Override
    public String toString(){
        return "Pozycja: " + this.position + ", orientacja: " + this.orientation;
    }

    public boolean isAt(Vector2d position){
        return this.position.x == position.x &&
               this.position.y == position.y;
    }

    public Vector2d getPosition(){
        return this.position;
    }
    public MapDirection getOrientation(){
        return this.orientation;
    }

// ogarnac @setUp do testowania
    public void move(MoveDirection direction){
        Vector2d possible_position;

        if (direction == null) {
            System.out.println("bledna wartosc");
            return;
        }

        switch (direction){
            case FORWARD -> {
                possible_position = this.position.add(this.orientation.toUnitVector());
                if(!possible_position.follows(new Vector2d(0,0)) || !possible_position.precedes(new Vector2d(4,4)))
                    return;
                this.position = possible_position;
            }
            case BACKWARD -> {
                possible_position = this.position.subtract(this.orientation.toUnitVector());
                if(!possible_position.follows(new Vector2d(0,0)) || !possible_position.precedes(new Vector2d(4,4)))
                    return;
                this.position = possible_position;
            }
            case LEFT -> this.orientation = this.orientation.previous();
            case RIGHT -> this.orientation = this.orientation.next();
        }
    }
}
