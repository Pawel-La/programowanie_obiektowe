package agh.ics.oop;
import agh.ics.oop.Direction.direction;

public class World {
    public static void move(direction arg){
        String message = switch (arg) {
            case FORWARD -> "Zwierzak idzie do przodu";
            case BACKWARD -> "Zwierzak idzie do tyłu";
            case RIGHT -> "Zwierzak skręca w prawo";
            case LEFT -> "Zwierzak skręca w lewo";
            default -> "None";
        };
        if (!message.equals("None")) {
            System.out.print(message);
            System.out.println();
        }
    }
    public static void run(direction[] args) {
        for(int i = 0; i < args.length - 1; i++) {
            move(args[i]);
        }
        move(args[args.length - 1]);
    }
    public static direction[] transform_to_enum(String[] args) {
        direction[] result = new direction[args.length];
        direction dir;

        for (int i = 0; i < args.length; i++) {
            dir = switch (args[i]){
                case "f" -> direction.FORWARD;
                case "b" -> direction.BACKWARD;
                case "r" -> direction.RIGHT;
                case "l" -> direction.LEFT;
                default -> direction.NONE;
            };
            result[i] = dir;
        }

        return result;
    }

    public static void main(String[] args) {
        String[] arguments = {"f", "f", "r", "l"};
        direction[] transformed = transform_to_enum(arguments);

        System.out.println("Start");
        run(transformed);
        System.out.println("Stop");
    }
}
