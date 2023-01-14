package agh.ics.oop;

public class CompletePredestination implements IBehaviorVariant {
    @Override
    public int updateActiveGene(int activeGene, int numberOfGenes) {
        return (activeGene + 1) % numberOfGenes;
    }
}
