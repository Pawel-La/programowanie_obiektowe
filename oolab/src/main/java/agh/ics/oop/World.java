package agh.ics.oop;

import java.util.Scanner;
public class World {
//    Odpowiedz na pytanie: jak zaimplementować mechanizm, który wyklucza pojawienie się dwóch zwierząt w tym samym miejscu.
//    Mozna dla kazdego miejsca na mapie przechowywac informacje czy jest zajete czy i na biezaco ją aktualizowac
//    Biorac pod uwage ze plansza jest mala (2. opcja to przechowywac polozenie wszystkich zwierzat ktore sa juz na mapie)
    public static void main(String[] args) {
//        Animal animal = new Animal();
//        System.out.println(animal);
//
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        String [] arguments = input.split("\\s+");
//
//        MoveDirection [] moves = OptionsParser.parse(arguments);
//        System.out.println("Start");
//        for (MoveDirection move: moves){
//            animal.move(move);
//            System.out.println(animal);
//        }
//        System.out.print("Stop");
        MoveDirection[] directions = new OptionsParser().parse(arguments);
        IWorldMap map = new RectangularMap(10, 5);
        Vector2d[] positions = { new Vector2d(2,2), new Vector2d(3,4) };
        IEngine engine = new SimulationEngine(directions, map, positions);
        engine.run();
    }
}
