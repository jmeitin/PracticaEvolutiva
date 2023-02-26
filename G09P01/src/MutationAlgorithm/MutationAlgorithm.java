package MutationAlgorithm;

import java.util.Random;

import GeneticAlgorithm.Chromosome;

public abstract class MutationAlgorithm {
	public abstract Chromosome[] mutate(Chromosome[] poblation, int poblation_size, double mutation_chance);
}
