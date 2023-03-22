package MutationAlgorithm.P2;

import java.util.Arrays;
import java.util.Random;

import Chromosomes.Chromosome;
import Chromosomes.ChromosomeP2;
import MutationAlgorithm.MutationAlgorithm;

public class HeuristicMutation extends MutationAlgorithm {
	
	static protected int num_selected_genes = 3;

	@Override
	public Chromosome[] mutate(Chromosome[] poblation, int poblation_size, double mutation_chance){
		int num_genes = poblation[0].getNumOfGenes();
		if(num_genes < 4) 
			return poblation;
		
		Chromosome[] new_population = new Chromosome[poblation_size];
		
		Random rand = new Random();
		int num_cases = calculateFactorial(num_selected_genes);
		
		int[] selected_nums = new int[num_selected_genes];		
		int [][] combinations = new int[num_cases][num_selected_genes];
		
		for (int i = 0; i < poblation_size; i++) {
			
			for (int n = 0; n < num_selected_genes; n++) {
				selected_nums[n] = (int)(rand.nextDouble()*(num_genes-1));			
			}
			
			for (int i1 = 0; i1 < num_cases; i1++) {
				for (int j = 0; j < num_selected_genes; j++) {
					combinations[i1][j] = selected_nums[j];
				}
			}
			
			///???????????????????????????????????????????????????'
			ChromosomeP2 chromosome = (ChromosomeP2) poblation[i].getCopy();
			
			// SELECT CUT POINTS--------------------------------------
			int[] points = new int[2];
			points[0] = 1 + (int)(rand.nextDouble() * (num_genes -2));
			points[1] = points[0];
			while (points[1] == points[0])
				points[1] = 1 + (int)(rand.nextDouble() * (num_genes -2));
			Arrays.sort(points);
			
			//APPLY DEFAULT VALUE-------------------------------------
			int[] new_genes = chromosome.getGenesCopy();
			
			// SWAP INNER SEGMENT-------------------------------------
			int[] swap = new int[points[1] - points[0]];
			
			for (int g = 0; g < swap.length / 2; g++) {
				int first = points[0] + g;
				int last = points[1] - g -1;
				int aux = new_genes[first];
				new_genes[first] = new_genes[last];
				new_genes[last] = aux;
			}
			
			chromosome.setGenes(new_genes);
			new_population[i] = chromosome;
		}

		return new_population;	
	}
	
	private int calculateFactorial (int num) {
		if(num <= 0)
			return 1;
		return num * calculateFactorial(num -1);
		
	}
	
	//int allelePoint = calculateNextPoint(rand, 1 * alleleLength, (length - 1) * alleleLength);
}
