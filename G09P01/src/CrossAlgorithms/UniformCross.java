package CrossAlgorithms;

import java.util.List;
import java.util.Random;

import GeneticAlgorithm.Chromosome;

public class UniformCross extends CrossAlgorithm {
	@Override
	protected void cross(Chromosome childA, Chromosome childB) {
		//GENES IN CHROMOSOME
		for (int g = 0; g < childA.getLenght(); g++) {
			//CROSS ALL ALLELES 
			childA.swapAllelesInGene(childB, g, 0, rand, 0.5); // Cross chance is (50%)
		}
	}
}