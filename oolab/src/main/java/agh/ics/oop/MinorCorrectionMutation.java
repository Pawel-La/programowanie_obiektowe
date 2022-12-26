package agh.ics.oop;

import java.util.Random;

public class MinorCorrectionMutation implements IMutationVariant{
    @Override
    public int getMutatedGene(int gene) {
        Random random = new Random();
        if(random.nextInt(2) == 0){
            return (gene+1) % 8;
        }
        return (gene+7) % 8;
    }
}
