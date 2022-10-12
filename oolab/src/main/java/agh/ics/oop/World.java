package agh.ics.oop;

import java.util.Scanner;
public class World {
    public static void move(Direction arg){
        String message = switch (arg) {
            case FORWARD -> "Zwierzak idzie do przodu";
            case BACKWARD -> "Zwierzak idzie do tyłu";
            case RIGHT -> "Zwierzak skręca w prawo";
            case LEFT -> "Zwierzak skręca w lewo";
        };
        System.out.println(message);
    }
    public static void run(Direction[] args) {
        for(Direction arg: args) {
            move(arg);
        }
    }
    public static Direction[] transform_to_enum(String[] args) {
        int outLength = 0;
        for (String arg : args) {
            if (arg.equals("f") || arg.equals("b") || arg.equals("r") || arg.equals("l")) {
                outLength++;
            }
        }

        Direction[] result = new Direction[outLength];
        Direction temporary;
        int i = 0;

        for (String arg: args) {
            temporary = switch (arg) {
                case "f" -> Direction.FORWARD;
                case "b" -> Direction.BACKWARD;
                case "r" -> Direction.RIGHT;
                case "l" -> Direction.LEFT;
                default -> null;
            };
            if (temporary != null) {
                result[i] = temporary;
                i++;
            }
        }
        return result;
    }

    public static void main(String[] args) {
//        testowanie działania metod z enuma MapDirection
//        MapDirection direction1 = MapDirection.NORTH;
//        System.out.println(direction1);
//        System.out.println(direction1.next());
//        System.out.println(direction1.previous());
//        System.out.println(direction1.toUnitVector());

//        Vector2d position1 = new Vector2d(1,2);
//        System.out.println(position1);
//        Vector2d position2 = new Vector2d(-2,1);
//        System.out.println(position2);
//        System.out.println(position1.add(position2));

        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        String [] arguments = input.split("\\s+");

        Direction[] transformed = transform_to_enum(arguments);
        System.out.println("Start");
        run(transformed);
        System.out.print("Stop");
    }
}
