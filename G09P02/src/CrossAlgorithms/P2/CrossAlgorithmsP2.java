package CrossAlgorithms.P2;

import java.util.HashSet;
import java.util.Set;

import Chromosomes.Chromosome;
import Chromosomes.ChromosomeP2;
import CrossAlgorithms.CrossAlgorithm;

public abstract class CrossAlgorithmsP2 extends CrossAlgorithm {

	@Override
	protected void cross(Chromosome first_child, Chromosome second_child) {
		this.cross((ChromosomeP2) first_child, (ChromosomeP2) second_child);
		this.checkCorrectness((ChromosomeP2) first_child, (ChromosomeP2) second_child);
	}

	private void checkCorrectness(ChromosomeP2 first_child, ChromosomeP2 second_child) {
		final int[] first_child_gene = first_child.getGenesRef();
		final int[] second_child_gene = second_child.getGenesRef();

		// Check there are no repeated genes using a Set
		Set<Integer> first_child_set = new HashSet<Integer>();
		Set<Integer> second_child_set = new HashSet<Integer>();

		for (int i = 0; i < first_child_gene.length; i++) {
			// If the gene is already in the set, it is repeated, throw an exception
			if (!first_child_set.add(first_child_gene[i])) {
				throw new RuntimeException("Repeated gene in first child");
			}

			if (!second_child_set.add(second_child_gene[i])) {
				throw new RuntimeException("Repeated gene in second child");
			}
		}
	}

	protected abstract void cross(ChromosomeP2 first_child, ChromosomeP2 second_child);
}
