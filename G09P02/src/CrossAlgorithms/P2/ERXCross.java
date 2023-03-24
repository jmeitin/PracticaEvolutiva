package CrossAlgorithms.P2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Chromosomes.ChromosomeP2;
import Utils.RandomUtils;

public class ERXCross extends CrossAlgorithmsP2 {

	@Override
	protected void cross(ChromosomeP2 first_child, ChromosomeP2 second_child) {
		final int[] first_child_gene = first_child.getGenesRef();
		final int[] second_child_gene = second_child.getGenesRef();

		HashMap<Integer, List<Integer>> table = buildTable(first_child_gene, second_child_gene);

		int[] first_child_new_genes = reconstructFromTable(first_child_gene, second_child_gene, table);
		int[] second_child_new_genes = reconstructFromTable(second_child_gene, first_child_gene, table);

		first_child.setGenes(first_child_new_genes);
		second_child.setGenes(second_child_new_genes);
	}

	protected static int[] reconstructFromTable(int[] child1, int[] child2, HashMap<Integer, List<Integer>> table) {
		int[] result = new int[child1.length];
		result[0] = child1[0];
		Set<Integer> result_set = new HashSet<Integer>();
		result_set.add(child1[0]); // Aux set for contain operation

		int n = child1.length;
		for (int i = 0; i < n - 1; i++) {

			List<Integer> neighbors = table.get(result[i]);
			int least_connected_neighbour = -1;
			boolean success = false;
			for (int neighbour : neighbors) {
				if (result_set.contains(neighbour))
					continue;

				// If they have the same amount of connections, randomlly choose between the old
				// and the new one
				final int neighbour_size = table.get(neighbour).size();
				final int least_connected_neighbour_size = least_connected_neighbour != -1
						? table.get(least_connected_neighbour).size()
						: Integer.MAX_VALUE;

				if (neighbour_size == least_connected_neighbour_size && RandomUtils.getProbability(50))
					least_connected_neighbour = neighbour;
				// Take the one with the least connections
				else if (neighbour_size < least_connected_neighbour_size)
					least_connected_neighbour = neighbour;

				success = true;
			}

			if (!success) // lock state, try again from start
				return reconstructFromTable(child1, child2, table);

			result[i + 1] = least_connected_neighbour;
			result_set.add(least_connected_neighbour);
		}

		return result;
	}

	// Bidirectional module function
	// module (1, 0, 2) // Output: 1
	// module (0, 0, 2) // Output: 0
	// module (-1, 0, 2)// Output: 1
	// module (3, 0, 2) // Output: 1
	protected static int module(int value, int min, int maxExclusive) {
		int range = maxExclusive - min;
		int mod = (value - min) % range;
		if (mod < 0) {
			mod += range;
		}
		return mod + min;
	}

	protected static HashMap<Integer, List<Integer>> buildTable(int[] child1, int[] child2) {
		HashMap<Integer, List<Integer>> table = new HashMap<>();
		int n = child1.length;
		for (int i = 0; i < n; i++) {

			// Get item and its neighbours
			int el = child1[i];
			int right_el = child1[module(i + 1, 0, n)];
			int left_el = child1[module(i - 1, 0, n)];

			// Add to table
			if (!table.containsKey(el))
				table.put(el, new ArrayList<>());

			List<Integer> list = table.get(el);
			if (!list.contains(left_el))
				list.add(left_el);
			if (!list.contains(right_el))
				list.add(right_el);

			// Repeat
			el = child2[i];
			right_el = child2[module(i + 1, 0, n)];
			left_el = child2[module(i - 1, 0, n)];

			if (!table.containsKey(el))
				table.put(el, new ArrayList<>());

			list = table.get(el);
			if (!list.contains(left_el))
				list.add(left_el);
			if (!list.contains(right_el))
				list.add(right_el);
		}
		return table;
	}
}
