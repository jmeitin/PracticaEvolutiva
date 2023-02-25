package CrossAlgorithms;

import java.util.Random;

import GeneticAlgorithm.Chromosome;

public abstract class CrossAlgorithm {
	public abstract Chromosome[] cross(Chromosome[] poblation, int poblation_size, double cross_chance, int num_points);
	
}
