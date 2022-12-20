package agh.ics.oop;

//public class OptionsParser {
//    public static MoveDirection [] parse (String [] args) throws IllegalArgumentException{
//        int len = 0;
//        for (String arg: args){
//            switch (arg){
//                case "f", "forward", "b", "backward", "r", "right", "l", "left" -> len++;
//                default -> throw new IllegalArgumentException(arg + " is not legal move specification");
//            }
//        }
//        MoveDirection [] result = new MoveDirection[len];
//        int i = 0;
//
//        for (String arg: args){
//            switch (arg) {
//                case "f", "forward" -> result[i++] = MoveDirection.FORWARD;
//                case "b", "backward" -> result[i++] = MoveDirection.BACKWARD;
//                case "r", "right" -> result[i++] = MoveDirection.RIGHT;
//                case "l", "left" -> result[i++] = MoveDirection.LEFT;
//            }
//        }
//
//        return result;
//    }
//}
