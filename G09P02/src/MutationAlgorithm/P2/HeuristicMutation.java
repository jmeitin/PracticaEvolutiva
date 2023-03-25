package MutationAlgorithm.P2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Chromosomes.Chromosome;
import Chromosomes.ChromosomeP2;
import MutationAlgorithm.MutationAlgorithm;
import Utils.RandomUtils;

/*
 * Se seleccionan n ciudades al azar (N = 3 en ejemplo). 
 * A continuación se generan todas las permutaciones de las ciudades seleccionadas. 
 * De todos los individuos que se generan con dichas permutaciones se selecciona el mejor. 
 * (Ejemplo: Seleccionas el 6, 4, 3. Las permutaciones son: 643, 634, 463, 436, 346 y 364. )
 * De todas las posibilidades generadas, eliges la mejor.
 * */
public class HeuristicMutation extends MutationAlgorithm {
	
	static protected int num_selected_genes = 3;

	@Override
	public Chromosome[] mutate(Chromosome[] poblation, int poblation_size, double mutation_chance){		
		int num_genes = poblation[0].getNumOfGenes();
		if(num_genes < 4) 
			return poblation;
		
		Chromosome[] new_population = new Chromosome[poblation_size];		
		int[] selected_index = new int[num_selected_genes];	
		
		for (int i = 0; i < poblation_size; i++) {		
			//initialize chromosome
			ChromosomeP2 chromosome = (ChromosomeP2) poblation[i].getCopy();	
			
			if(RandomUtils.getProbability(mutation_chance)) {
				//CALCULATE RANDOM INDEX--------------------------------------------
				for (int n = 0; n < num_selected_genes; n++) {
					int value = (int)(rand.nextDouble()*(num_genes-1));
					
					//DON'T REPEAT VALUES
					while(contains(selected_index, value, i)) {
						value = (int)(rand.nextDouble()*(num_genes-1));
					}
					selected_index[n] = value;	
				}
				//SORT
				Arrays.sort(selected_index);
				
				// CALCULATE PERMUTATIONS				
				List<int[]> permutations = CalculatePermutations(selected_index);
				
				// SELECT PERMUTATION WITH BEST SCORE
				int best_score = Integer.MAX_VALUE;
				int[] old_genes = chromosome.getGenesCopy();
				int[] new_genes = new int[num_genes];
				ChromosomeP2 aux_chromosome = (ChromosomeP2)chromosome.getCopy();
				
				for (int l = 0; l < permutations.size(); l++) {
					int index = 0;
					int[] permutation = permutations.get(l);
					
					// USE PERMUTATION TO GENERATE new_genes POSSIBLE CONFIGURATION
					for(int g = 0; g < num_genes; g++) {
						//INSERT
						int old_index = g;
						if(index < permutation.length && permutation[index] == g) {
							old_index = permutation[index];
							index++;
						}

						new_genes[g] = old_genes[old_index];
					}
					
					// DETERMINE IF POSSIBLE CONFIGURATION HAS BETTER SCORE YET
					aux_chromosome.setGenes(new_genes);
					aux_chromosome.calculateFenotypes();
					double brute_fitness = aux_chromosome.evaluate();
					if(brute_fitness < best_score) {
						best_score = (int)brute_fitness;
						chromosome = (ChromosomeP2)aux_chromosome.getCopy();
					}
				}
			}
			new_population[i] = chromosome.getCopy();
		}

		return new_population;	
	}
	
	/*
	 * CALCULATE PERMUTATIONS OF INTS IN ARRAY numbers
	 * RETURNS List<int[]> WITH ALL POSSIBLE PERMUTATIONS OF numbers
	 * */
	public static List<int[]> CalculatePermutations(final int[] numbers) {
		List<int[]> permutations = new ArrayList<int[]>();
		
	    // RETURN CURRENT ELEMENT BY DEFAULT
		if(numbers.length == 0) {
			return permutations;
		}
		else if (numbers.length == 1) {
	    	permutations.add(numbers);
	      return permutations;
	    }

	    // IF LENGTH > 1 ==> CALCULATE SUB PERMUTATIONS
	    for (int i = 0; i < numbers.length; i++) {
	      // CREATE AUX ARRAY WITHOUT CURRENT ELEMENT-------------------------------
	      int[] aux = new int[numbers.length - 1];
	      // arraycopy: "Método que copia desde la posición origen de un array a un array destino en una posición específica."
	      // src = numbers, src_pos = 0, dst = aux, dst_pos = 0, num_eltos_copiados = i;
	      // COPY ELEMENTS BEFORE i:
	      System.arraycopy(numbers, 0, aux, 0, i);
	      // COPY ELEMENTS AFTER i:
	      System.arraycopy(numbers, i + 1, aux, i, numbers.length - i - 1);

	      // RECURSIVE CALL. CALCULATE SUB PERMUTATIONS OF AUX-----------------------
	      List<int[]> sub_permutations = CalculatePermutations(aux);

	      // ADD ELEMENT i TO SUB PERMUTATIONS---------------------------------------
	      for (int j = 0; j < sub_permutations.size(); j++) {
	        int[] perm = new int[numbers.length];
	        // insert i at the start
	        perm[0] = numbers[i]; 
	        // copy sub_permutations after i
	        System.arraycopy(sub_permutations.get(j), 0, perm, 1, aux.length);
	        permutations.add(perm);
	      }
	    }

	    // Devuelve todas las permutaciones
	    return permutations;
	  }
	
	private boolean contains(int[] array, int v, final int i_max) {
		if (i_max >= array.length || i_max < 0)
			return false;
		
        int i = 0;
        while (i <= i_max && array[i] != v)
        	i++;

        return i <= i_max; // true if value was found
    }
}
