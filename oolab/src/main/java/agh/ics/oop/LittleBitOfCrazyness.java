package agh.ics.oop;

import java.util.Random;

public class LittleBitOfCrazyness implements IBehaviorVariant{
    @Override
    public int updateActiveGenome(int activeGenome, int number_of_genomes) {
        Random rand = new Random();
        if (rand.nextInt(5) == 0)
            return (activeGenome + 1) % number_of_genomes;
        return rand.nextInt(number_of_genomes);
    }
}
