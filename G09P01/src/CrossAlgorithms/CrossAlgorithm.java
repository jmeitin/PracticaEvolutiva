package CrossAlgorithms;

import java.util.Random;

import GeneticAlgorithm.Chromosome;

public abstract class CrossAlgorithm {
	public abstract Chromosome[] cross(Chromosome[] poblation, int poblation_size, double cross_chance, int num_points);
	
	protected int calculateNextPoint(Random rand, int start, int end) {
		//Point belongs to [1, length - 1]
		// if length = 10 & point = 0.3 ==> 1 + 0.3 * (10 - 2) = 2.7 ==> 2nd gene
		// if point 1.0 ==> 1 + 1 * 8 = 9 ==> 9th gene
		double aux = start + rand.nextDouble() * (end - start); 
		int point = (int)aux;
		
		return point;
	}
}
