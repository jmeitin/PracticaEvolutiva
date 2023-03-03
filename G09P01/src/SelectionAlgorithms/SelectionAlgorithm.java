package SelectionAlgorithms;

import GeneticAlgorithm.Chromosome;

public abstract class SelectionAlgorithm {
	public abstract Chromosome[] select(Chromosome[] poblation, int poblation_size);
}
