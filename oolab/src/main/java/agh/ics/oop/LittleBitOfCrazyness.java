package agh.ics.oop;

import java.util.Random;

public class LittleBitOfCrazyness implements IBehaviorVariant{
    @Override
    public int updateActiveGene(int activeGene, int numberOfGenes) {
        Random rand = new Random();
//        20% chance for random genome
        if (rand.nextInt(5) == 0){
            int updatedGene = activeGene;
            while (updatedGene == activeGene)
                updatedGene = rand.nextInt(numberOfGenes);
            return updatedGene;
        }

        return (activeGene + 1) % numberOfGenes;
    }
}
