package CrossAlgorithms.P2;

import java.util.HashSet;
import java.util.Set;

import Chromosomes.ChromosomeP2;
import Utils.RandomUtils;

public class PMXCross extends CrossAlgorithmsP2 {

	@Override
	protected void cross(ChromosomeP2 first_child, ChromosomeP2 second_child) {
		final int gene_size = first_child.getNumOfGenes();
		final int last_index = gene_size - 1;
		
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
		do
		{
			first_cross = RandomUtils.getRandomInt(gene_size);
			second_cross = RandomUtils.getRandomInt(gene_size);
			
			// First cross must be the smaller index, if not, exchange with second cross
			if(first_cross > second_cross)
			{
				int tmp = first_cross;
				first_cross = second_cross;
				second_cross = tmp;
			}
		} while(second_cross - first_cross == gene_size || first_cross == second_cross); 
	
		// Swap between cross points
		for(int i = first_cross; i < second_cross; i++)
		{
			first_child_gen_copy[i] = second_child_gene[i];
			first_child_new_gen.add(second_child_gene[i]);
			
			second_child_gen_copy[i] = first_child_gene[i];
			second_child_new_gen.add(first_child_gene[i]);
		}
		
		int gene_to_fill = second_cross - first_cross;
		
		// Iterate the next genes without getting out of the array
		for(int next_index = second_cross % last_index; gene_to_fill > 0; gene_to_fill--, ++next_index, next_index %= last_index)
		{
			if(first_child_new_gen.contains(first_child_gene[next_index]))
			{
				
			}
			
			if(second_child_new_gen.contains(second_child_gene[next_index]))
			{
				
			}
		}
		
	}
}
