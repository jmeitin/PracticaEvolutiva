package CrossAlgorithms.P2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import Chromosomes.ChromosomeP2;
import Utils.ArrayUtils;
import Utils.RandomUtils;

public class PMXCross extends CrossAlgorithmsP2 {

	@Override
	protected void cross(ChromosomeP2 first_child, ChromosomeP2 second_child) {
		final int gene_size = first_child.getNumOfGenes();

		final int[] first_child_gene = first_child.getGenesRef();
		final int[] second_child_gene = second_child.getGenesRef();

		int[] first_child_gen_copy = first_child.getGenesCopy();
		int[] second_child_gen_copy = second_child.getGenesCopy();

		Set<Integer> first_child_new_gen = new HashSet<Integer>();
		Set<Integer> second_child_new_gen = new HashSet<Integer>();

		// Select range
		int first_cross = 0;
		int second_cross = 0;

		// Avoid full range and empty range
		do {
			first_cross = RandomUtils.getRandomInt(gene_size);
			second_cross = RandomUtils.getRandomInt(gene_size);

			// First cross must be the smaller index, if not, exchange with second cross
			if (first_cross > second_cross) {
				int tmp = first_cross;
				first_cross = second_cross;
				second_cross = tmp;
			}
		} while (second_cross - first_cross == gene_size || first_cross == second_cross);

		// Swap between cross points, set the other to unknown (-1)
		for (int i = 0; i < gene_size; i++) {
			if (i >= first_cross && i < second_cross) {
				first_child_gen_copy[i] = second_child_gene[i];
				first_child_new_gen.add(second_child_gene[i]);

				second_child_gen_copy[i] = first_child_gene[i];
				second_child_new_gen.add(first_child_gene[i]);
			} else {
				first_child_gen_copy[i] = -1;
				second_child_gen_copy[i] = -1;
			}
		}

		// Iterate fron 0 to end of gene except for genes in cross interval. If gene is
		// repeated, exchange with the homologus in the other gene.
		// If the homologous if repeated, search next homologous
		// If gene isn't already in child, we just add it.
		for (int i = 0; i < gene_size; i++) {
			// These are already exchanged
			if (i >= first_cross && i < second_cross)
				continue;

			// First child
			// If element is already in the genes, search homologous
			int current_gen = first_child_gene[i];
			if (first_child_new_gen.contains(first_child_gene[i])) {
				while (first_child_new_gen.contains(current_gen)) {
					final int gene = first_child_gene[i];

					// First, search index of gene in current child
					final int gene_index = ArrayUtils.indexOf(first_child_gen_copy, gene);
					current_gen = second_child_gen_copy[gene_index];
				}

			}

			// Now we have the correct gene we can assign it
			first_child_gen_copy[i] = current_gen;
			first_child_new_gen.add(current_gen);
			
			// Second child
			// If element is already in the genes, search homologous
			current_gen = second_child_gene[i];
			if (second_child_new_gen.contains(second_child_gene[i])) {
				current_gen = second_child_gene[i];
				while (second_child_new_gen.contains(current_gen)) {
					final int gene = second_child_gene[i];

					// First, search index of gene in current child
					final int gene_index = ArrayUtils.indexOf(second_child_gen_copy, gene);
					current_gen = first_child_gen_copy[gene_index];
				}

			}
			
			// Now we have the correct gene we can assign it
			second_child_gen_copy[i] = current_gen;
			second_child_new_gen.add(current_gen);
		}

		first_child.setGenes(first_child_gen_copy);
		second_child.setGenes(second_child_gen_copy);
	}
}
