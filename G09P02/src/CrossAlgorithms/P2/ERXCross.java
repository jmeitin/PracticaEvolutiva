package CrossAlgorithms.P2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Chromosomes.ChromosomeP2;

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

	// Reconstruct a Hamiltonian path from two children and a table of adjacent
	// vertices
	// Side note: We now we are building a Hamiltonian path because we know the
	// graph is connected, has n vertex and we visit each one of them only once
	// We use backtracking to try every posible path. But somehow this still doesn't
	// work and everyonce in a while gets stuck in a infinite loop
	protected static int[] reconstructFromTable(int[] child1, int[] child2, HashMap<Integer, List<Integer>> table) {
		int[] result = new int[child1.length];
		result[0] = child1[0];
		Set<Integer> result_set = new HashSet<Integer>();
		result_set.add(child1[0]);

		// Use helper function to recursively explore paths until a valid Hamiltonian
		// path is found. It must always return a valid result
		if(!reconstructFromTableHelper(result, result_set, table, 1))
			throw new RuntimeException("ERX Cross failed");
		
		return result;
	}

	// Recursive helper function to explore potential paths for Hamiltonian paths
	protected static boolean reconstructFromTableHelper(int[] result, Set<Integer> result_set,
			HashMap<Integer, List<Integer>> table, int i) {

		// Base case: if all vertices have been added to the path, return true
		if (i == result.length)
			return true;

		// Get the neighbors of the last added vertex and choose the least connected
		// neighbor
		List<Integer> neighbors = table.get(result[i - 1]);

		// Sort neighbours, so we explore first the ones with less connections
		Collections.sort(neighbors, (v1, v2) -> Integer.compare(table.get(v1).size(), table.get(v2).size()));

		for (int neighbour : neighbors) {
			if (result_set.contains(neighbour))
				continue;
			
			// Add the least connected neighbor to the path and continue recursively
			// As neighbours are sorted we can just try them in order
			// exploring paths
			result[i] = neighbour;
			result_set.add(neighbour);

			// If a valid Hamiltonian path is found, return true
			if (reconstructFromTableHelper(result, result_set, table, i + 1))
				return true;
			else 
				// If a valid path is not found, backtrack and try a different neighbor
				result_set.remove(neighbour);
		}

		// If no valid path is found, return false
		return false;
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
