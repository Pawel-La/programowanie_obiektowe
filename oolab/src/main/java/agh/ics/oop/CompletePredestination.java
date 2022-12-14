package agh.ics.oop;

public class CompletePredestination implements IBehaviorVariant{
    @Override
    public int updateActiveGenome(int activeGenome, int number_of_genomes) {
        return (activeGenome + 1) % number_of_genomes;
    }
}
