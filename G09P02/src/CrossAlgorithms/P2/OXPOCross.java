package CrossAlgorithms.P2;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import Chromosomes.ChromosomeP2;
import Utils.RandomUtils;

public class OXPOCross extends CrossAlgorithmsP2 {

	@Override
	protected void cross(ChromosomeP2 first_child, ChromosomeP2 second_child) {
		final int gene_size = first_child.getNumOfGenes();

		final int[] first_child_gene = first_child.getGenesRef();
		final int[] second_child_gene = second_child.getGenesRef();

		int[] first_child_gen_copy = first_child.getGenesCopy();
		int[] second_child_gen_copy = second_child.getGenesCopy();

		ArrayList<Integer> first_child_prioritary_elements = new ArrayList<Integer>();
		ArrayList<Integer> second_child_prioritary_elements = new ArrayList<Integer>();

		// Select range
		SortedSet<Integer> prioritary_positions = new TreeSet<Integer>();

		// Avoid full range and empty range
		do {
			final int position = RandomUtils.getRandomInt(gene_size - 1);
			// Only add the new element if it wasn't already added
			if (prioritary_positions.add(position)) {
				first_child_prioritary_elements.add(first_child_gene[position]);
				second_child_prioritary_elements.add(second_child_gene[position]);
			}
		} while (RandomUtils.getProbability(75)); // 75% probabilitie to choose another priority_position

		// Fill the children, if the opposite child element is not a prioritary element
		// in the other child
		// Then we add it, if not, we add the next prioritary element from our child
		int ch1 = 0, ch2 = 0;
		for (int i = 0; i < gene_size; i++) {
			if (first_child_prioritary_elements.contains(second_child_gene[i]))
				first_child_gen_copy[i] = first_child_prioritary_elements.get(ch1++);
			else
				first_child_gen_copy[i] = second_child_gene[i];

			if (second_child_prioritary_elements.contains(first_child_gene[i]))
				second_child_gen_copy[i] = second_child_prioritary_elements.get(ch2++);
			else
				second_child_gen_copy[i] = first_child_gene[i];
		}

		first_child.setGenes(first_child_gen_copy);
		second_child.setGenes(second_child_gen_copy);
	}

}
