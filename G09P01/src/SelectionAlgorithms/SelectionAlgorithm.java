package SelectionAlgorithms;

import Chromosomes.Chromosome;

public abstract class SelectionAlgorithm {
	public abstract Chromosome[] select(Chromosome[] poblation, int poblation_size);
}
