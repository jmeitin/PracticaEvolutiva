package MutationAlgorithm;

import java.util.Arrays;

import Chromosomes.Chromosome;
import Chromosomes.Gene;

//A NIVEL DE GEN

public class InsertionMutation extends MutationAlgorithm{
	
	protected static int  inserted_num = 1;
	
	@Override
	public Chromosome[] mutate(Chromosome[] poblation, int poblation_size, double mutation_chance){
		if (inserted_num > poblation_size) 
			inserted_num = 1;
		
		int num_genes = poblation[0].getNumOfGenes();
		int [] can_insert = new int[inserted_num]; // indicates if allele will be inserted
		Chromosome[] new_population = new Chromosome[poblation_size];
		
		for (int i = 0; i < poblation_size; i++) {			
			//CALCULATE INSERTION POINTS-------------------------------
			for(int j = 0; j < inserted_num; j++) {
				can_insert[j] = (int)(rand.nextDouble() * (num_genes - 1));
			}
			Arrays.sort(can_insert); //order array
			
			//MUTATE CHROMOSOME---------------------------------------------------
			new_population[i] = poblation[i].getCopy();
			
			int i_insert = 0;
			//GENES IN CHROMOSOME
			for (int g = 0; g < num_genes; g ++) {				
				if(can_insert[i_insert] == g) {
					int other = g + (int)(rand.nextDouble() * (num_genes - g - 1)); //another gene in [g, L-1]
					new_population[i].displaceGenes(g, other);
				}					
				
				i_insert++;
			}
		}

		return new_population;	
	}
	
	public void setInsertedNum(int n) {
		inserted_num = n;
	}
}
