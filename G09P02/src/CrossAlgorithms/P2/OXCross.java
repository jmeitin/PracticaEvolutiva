package CrossAlgorithms.P2;

import java.util.HashSet;
import java.util.Set;

import Chromosomes.ChromosomeP2;
import Utils.ArrayUtils;
import Utils.RandomUtils;

public class OXCross extends CrossAlgorithmsP2 {

	@Override
	protected void cross(ChromosomeP2 first_child, ChromosomeP2 second_child) {
		final int gene_size = first_child.getNumOfGenes();

		first_child.setGenes(new int[]{1,2,3,5,6,4});
		second_child.setGenes(new int[]{4,3,2,1,5,6});
		
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

		first_cross = 2;
		second_cross = 4;
		
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

		final int items_to_fill = gene_size - (second_cross - first_cross);

		// Iterate from second_cross items_to_fill times. We have indexes for each child original array
		// And we use module operation to find out the current index in our child copy we are assigning
		for (int i = second_cross, ch1 = second_cross, ch2 = second_cross; // Init 
				 i < items_to_fill + second_cross; 						   // Condition
				 i++,ch1 = (ch1 + 1) % gene_size, ch2 = (ch2 + 1) % gene_size) // Increment 
		{
			// If the gene is already in the child, ignore it
			while (first_child_new_gen.contains(first_child_gene[ch1])) {
				ch1 = (ch1 + 1) % gene_size;
				continue;
			}

			first_child_gen_copy[i % gene_size] = first_child_gene[ch1];
			//first_child_new_gen.add(first_child_gene[ch1]); Not necessary, we only care about the numbers inserted in the swap

			while (second_child_new_gen.contains(second_child_gene[ch2])) {
				ch2 = (ch2 + 1) % gene_size;
				continue;
			}

			second_child_gen_copy[i % gene_size] = second_child_gene[ch2];
			//second_child_new_gen.add(second_child_gene[ch2]); Not necessary, we only care about the numbers inserted in the swap
		}

		first_child.setGenes(first_child_gen_copy);
		second_child.setGenes(second_child_gen_copy);
	}
}
