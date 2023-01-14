package agh.ics.oop;

import java.util.Random;

public class CompletelyRandomMutation implements IMutationVariant {
    @Override
    public int getMutatedGene(int gene) {
        Random random = new Random();  // nowy obiekt co wywołanie
        return random.nextInt(8);
    }
}
