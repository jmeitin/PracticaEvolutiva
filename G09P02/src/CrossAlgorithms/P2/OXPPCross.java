package CrossAlgorithms.P2;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import Chromosomes.ChromosomeP2;
import Utils.RandomUtils;

public class OXPPCross extends CrossAlgorithmsP2 {

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
		SortedSet<Integer> prioritary_positions = new TreeSet<Integer>();

		// Avoid full range and empty range
		do {
			prioritary_positions.add(RandomUtils.getRandomInt(gene_size));
		} while (RandomUtils.getProbability(75)); // 75% probabilitie to choose another priority_position
		
		// Swap between cross points, set the other to unknown (-1)
		for (int i = 0; i < gene_size; i++) {
			if (prioritary_positions.contains(i)) {
				first_child_gen_copy[i] = second_child_gene[i];
				first_child_new_gen.add(second_child_gene[i]);

				second_child_gen_copy[i] = first_child_gene[i];
				second_child_new_gen.add(first_child_gene[i]);
			} else {
				first_child_gen_copy[i] = -1;
				second_child_gen_copy[i] = -1;
			}
		}

		final int items_to_fill = gene_size;
		final int latest_position = prioritary_positions.last();

		// Iterate from second_cross items_to_fill times. We have indexes for each child original array
		// And we use module operation to find out the current index in our child copy we are assigning
		for (int i = latest_position, ch1 = latest_position % gene_size, ch2 = latest_position % gene_size; // Init 
				 i < items_to_fill + latest_position; 						   // Condition
				 i++) // Increment 
		{
			// Ignore prioritary positions
			if(prioritary_positions.contains(i % gene_size))
				continue;
			
			// If the gene is already in the child, ignore it
			while (first_child_new_gen.contains(first_child_gene[ch1])) {
				ch1++;
				if(ch1 >= gene_size)
					ch1 = 0;
			}

			first_child_gen_copy[i % gene_size] = first_child_gene[ch1];
			//first_child_new_gen.add(first_child_gene[ch1]); Not necessary, we only care about the numbers inserted in the swap

			while (second_child_new_gen.contains(second_child_gene[ch2])) {
				ch2++;
				if(ch2 >= gene_size)
					ch2 = 0;
			}

			second_child_gen_copy[i % gene_size] = second_child_gene[ch2];
			//second_child_new_gen.add(second_child_gene[ch2]); Not necessary, we only care about the numbers inserted in the swap
			
			// Update aux ch indexes
			ch1 = (ch1 + 1) % gene_size;
			ch2 = (ch2 + 1) % gene_size;	
		}

		first_child.setGenes(first_child_gen_copy);
		second_child.setGenes(second_child_gen_copy);
	}

}
