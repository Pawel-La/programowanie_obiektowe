package agh.ics.oop;

public interface IBehaviorVariant {
    /**
     * @param activeGene - active gene
     * @param numberOfGenes - number of animal genes
     * @return new active gene number
     */
    int updateActiveGene(int activeGene, int numberOfGenes);
}
