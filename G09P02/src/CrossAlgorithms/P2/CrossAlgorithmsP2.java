package CrossAlgorithms.P2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
				String error_message = "\nRepeated gene in first child.";
				error_message += " Num of elements: " + first_child_gene.length;
				error_message += "\nGene: " + first_child_gene[i];
				error_message += "\nIndexes: " + formatArray(getAllIndex(first_child_gene, first_child_gene[i]));
				error_message += "\nGenes:" + formatArray(first_child_gene);
				error_message += "\nGenes:" + formatArray(fillArray(second_child_gene));
				throw new RuntimeException(error_message);
			}

			if (!second_child_set.add(second_child_gene[i])) {
				String error_message = "\nRepeated gene in second child.";
				error_message += " Num of elements: " + second_child_gene.length;
				error_message += "\nGene: " + second_child_gene[i];
				error_message += "\nIndex: " + formatArray(getAllIndex(second_child_gene, second_child_gene[i]));
				error_message += "\nGenes:" + formatArray(second_child_gene);
				error_message += "\nGenes:" + formatArray(fillArray(second_child_gene));
				throw new RuntimeException(error_message);
			}
		}
	}

	public static String formatArray(int[] array) {
		String text = "[";
		for (int i = 0; i < array.length; i++) {
			int num = array[i];
			if (num < 10)
				text += "0";

			text += num + ", ";
		}

		text = text.substring(0, text.length() - 2);
		text += "]";
		return text;
	}

	public static int[] fillArray(int[] array) {
		int new_array[] = new int[array.length];

		for (int i = 0; i < array.length; i++)
			new_array[i] = i;

		return new_array;
	}

	public static int getIndex(final int[] array, final int element) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == element)
				return i;
		}

		return -1;
	}

	public static int[] getAllIndex(final int[] array, final int element) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < array.length; i++) {
			if (array[i] == element)
				list.add(i);
		}

		int array_out[] = new int[list.size()];

		for (int i = 0; i < list.size(); i++)
			array_out[i] = list.get(i);

		return array_out;
	}

	protected abstract void cross(ChromosomeP2 first_child, ChromosomeP2 second_child);
}
