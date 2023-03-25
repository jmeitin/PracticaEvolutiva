package CrossAlgorithms.P2;

import java.util.HashSet;
import java.util.Set;

import Chromosomes.ChromosomeP2;

public class CXCross extends CrossAlgorithmsP2 {

	@Override
	protected void cross(ChromosomeP2 first_child, ChromosomeP2 second_child) {
		final int[] first_child_gene = first_child.getGenesRef();
		final int[] second_child_gene = second_child.getGenesRef();
		
		first_child.setGenes(getChildFromCicle(first_child_gene,second_child_gene));
		second_child.setGenes(getChildFromCicle(second_child_gene,first_child_gene));
	}

	int[] getChildFromCicle(final int[] child1, final int[] child2)
	{
		int[] result = new int[child1.length];
		Set<Integer> first_child_new_gen = new HashSet<Integer>();
		
		// Init algorithm
		int current_index = 0;
		result[current_index] = child1[current_index];
		first_child_new_gen.add(result[current_index]);
		current_index = getIndex(child1, child2[current_index]);
		
		// Do a cycle. For this we asign the current value, and then we look which one is the corresponding one ine the other child
		do
		{
			result[current_index] = child1[current_index];
			first_child_new_gen.add(result[current_index]);
			current_index = getIndex(child1, child2[current_index]);
		}while(!first_child_new_gen.contains(result[current_index]));
		
		// Finally, we fill the rest of the array with the remaining values from the other child
		// If the value is not in the set, we put the value in that index from the other child
		for(int i = 0; i < result.length; i++)
			if(!first_child_new_gen.contains(result[i]))
				result[i] = child2[i];
		
		return result;
	}
 }
