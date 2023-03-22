package MutationAlgorithm;

import java.util.Arrays;

import Chromosomes.Chromosome;
import Chromosomes.ChromosomeP2;
import Chromosomes.Gene;

//A NIVEL DE GEN

public class InsertionMutation extends MutationAlgorithm{
	
	protected static int  inserted_num = 1;
	
	@Override
	public Chromosome[] mutate(Chromosome[] poblation, int poblation_size, double mutation_chance){
		if (inserted_num > poblation_size) 
			inserted_num = 1;		
		
		int num_genes = poblation[0].getNumOfGenes();
		int [] move_pos = new int[inserted_num]; // indicates genes that will be moves
		int [] insert_pos = new int[inserted_num];
		Chromosome[] new_population = new Chromosome[poblation_size];
		
		for (int i = 0; i < poblation_size; i++) {			
			//CALCULATE RANDOM INSERTION POINTS & NUMBERS TO INSERT-------------------------------
			for(int j = 0; j < inserted_num; j++) {
				move_pos[j] = (int)(rand.nextDouble() * (num_genes - 1));
				insert_pos[j] = (int)(rand.nextDouble() * (num_genes - 1));
			}
			Arrays.sort(move_pos); //order array <
			Arrays.sort(insert_pos); //order array <
			
			//MUTATE CHROMOSOME---------------------------------------------------
			ChromosomeP2 chromosome = (ChromosomeP2)poblation[i].getCopy();
			
			int[] genes = chromosome.getGenesCopy();
			int[] new_genes = new int[num_genes];
			int i_insert = 0;
			// INSERT NUMBERS FIRST---------------------------------------------------
			for(int g = 0; g < num_genes; g++) {
				// I want to insert number here
				if(insert_pos[i_insert] == g) {
					//Pos where number that wants to be inserted is found
					int old_pos = move_pos[i_insert];
					//Move gene
					new_genes[g] = genes[old_pos];

					//IN CASE THAT A NUMBER (move_pos or insert_pos) WAS SELECTED MULTIPLE TIMES
					if (i_insert + 1 < inserted_num && 
							(move_pos[i_insert] == move_pos[i_insert + 1] || insert_pos[i_insert] == insert_pos[i_insert + 1])) {
						while (i_insert + 1 < inserted_num && 
								(move_pos[i_insert] == move_pos[i_insert + 1] || insert_pos[i_insert] == insert_pos[i_insert + 1]))
							i_insert++;
					}
					//MOVE TO NEXT INSERT NUMBER
					else { 
						i_insert++;
					}
				}
				// NON VALID DISTANCE
				else new_genes[g] = -1; 
			}

			// DISPLACE NUMBERS WITH NON VALID DISTANCE-----------------------------------------------
			int old_pos = 0;
			int new_pos = 0;
			while (new_pos < num_genes && old_pos < num_genes) {
				
				// THIS NUMBER HAS ALREADY BEEN INSERTED ELSEWHERE
				while(old_pos < num_genes && numberExistsInArray(move_pos, old_pos)) {
					old_pos++;
				}
				// THIS IS AN INSERTED NUMBER (For loop above)
				while(new_pos < num_genes && new_genes[new_pos] != -1) {
					new_pos++;
				}
				// WE HAVE A VALID move_pos & insert_pos
				if (new_pos < num_genes && old_pos < num_genes) {
					new_genes[new_pos] = genes[old_pos];
					new_pos++;
					old_pos++;					
				}
			}		

			new_population[i] = chromosome;
		}

		return new_population;	
	}
	
	private boolean numberExistsInArray(int[] positions, int value) {
		int i = 0; 
		while (i < positions.length && positions[i] != value)
			i++;
		//returns true if value was found in array
		return i != positions.length; 
	}
	
	public void setInsertedNum(int n) {
		inserted_num = n;
	}
}
