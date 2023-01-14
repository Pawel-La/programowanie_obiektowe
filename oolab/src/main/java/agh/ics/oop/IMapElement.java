package agh.ics.oop;

public interface IMapElement {
    /**
     * @return file path to map element look
     */
    String getMapElementLookFile();

    String toString();

    /**
     * @return position of this map element
     */
    Vector2d getPosition();
}
