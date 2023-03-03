package CrossAlgorithms;

import GeneticAlgorithm.Chromosome;

public class UniformCross extends CrossAlgorithm {
	@Override
	protected void cross(Chromosome first_child, Chromosome second_child) {
		// Genes in chromosome
		for (int g = 0; g < first_child.getLenght(); g++) {
			// Cross all alleles
			first_child.swapAllelesInGene(second_child, g, 0, rand, 0.5); // Cross chance is (50%)
		}
	}
}