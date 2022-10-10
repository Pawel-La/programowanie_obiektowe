package agh.ics.oop;

public class World {
    public static void move(Direction arg){
        String message = switch (arg) {
            case FORWARD -> "Zwierzak idzie do przodu";
            case BACKWARD -> "Zwierzak idzie do tyłu";
            case RIGHT -> "Zwierzak skręca w prawo";
            case LEFT -> "Zwierzak skręca w lewo";
            default -> "None";
        };
        if (!message.equals("None")) {
            System.out.println(message);
        }
    }
    public static void run(Direction[] args) {
        for(Direction arg: args) {
            move(arg);
        }
    }
    public static Direction[] transform_to_enum(String[] args) {
        Direction[] result = new Direction[args.length];

        for (int i = 0; i < args.length; i++) {
            result[i] = switch (args[i]){
                case "f" -> Direction.FORWARD;
                case "b" -> Direction.BACKWARD;
                case "r" -> Direction.RIGHT;
                case "l" -> Direction.LEFT;
                default  -> Direction.NONE;
            };
        }

        return result;
    }

    public static void main(String[] args) {
        String[] arguments = {"f", "f", "r", "l"};
        Direction[] transformed = transform_to_enum(arguments);

        System.out.println("Start");
        run(transformed);
        System.out.print("Stop");
    }
}
