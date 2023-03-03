package CrossAlgorithms;

import GeneticAlgorithm.Chromosome;

public class UniformCross extends CrossAlgorithm {
	@Override
	protected void cross(Chromosome childA, Chromosome childB) {
		// Genes in chromosome
		for (int g = 0; g < childA.getLenght(); g++) {
			// Cross all alleles
			childA.swapAllelesInGene(childB, g, 0, rand, 0.5); // Cross chance is (50%)
		}
	}
}