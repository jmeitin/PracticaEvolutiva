package MutationAlgorithm;

import java.util.Random;

import Chromosomes.Chromosome;

public abstract class MutationAlgorithm {
	protected Random rand = new Random();
	
	/**
	 * Mutates a poblation of chromosomes using a strategy
	 * @param poblation Cromosome poblation
	 * @param poblation_size Poblation size
	 * @param mutation_chance Chance to mutate gen (0-100%)
	 * @return
	 */
	public abstract Chromosome[] mutate(Chromosome[] poblation, int poblation_size, double mutation_chance);
}
