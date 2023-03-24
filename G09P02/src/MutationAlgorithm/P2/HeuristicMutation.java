package MutationAlgorithm.P2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import Chromosomes.Chromosome;
import Chromosomes.ChromosomeP2;
import MutationAlgorithm.MutationAlgorithm;

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
		int[] index_in_permutation = new int[num_selected_genes];	
		
		for (int i = 0; i < poblation_size; i++) {		
			//initialize chromosome
			ChromosomeP2 chromosome = (ChromosomeP2) poblation[i].getCopy();	
			
			if(rand.nextDouble() < mutation_chance) {
				//CALCULATE RANDOM INDEX--------------------------------------------
				for (int n = 0; n < num_selected_genes; n++) {
					index_in_permutation[n] = (int)(rand.nextDouble()*(num_genes-1));	
				}
				//SORT
				Arrays.sort(index_in_permutation);
				
				//calculate permutations 
				List<int[]> permutations = CalculatePermutations(index_in_permutation);
				
				// LOOP ALL POSSIBLE PERMUTATIONS--------------------------------
				List<int[]> all_possible_genes = new ArrayList<int[]>();			
				int[] current_genes = chromosome.getGenesCopy();			
				
				for (int p = 0; p < permutations.size(); p++) {
					int[] possible_genes = current_genes.clone();
					
					int [] permutation = permutations.get(p);
					for (int elem = 0; elem < permutation.length; elem++) {
						int new_index = index_in_permutation[elem]; // sorted array
						int old_index = permutation[elem];
						// UPDATE GENE
						possible_genes[new_index] = current_genes[old_index];
					}
					
					all_possible_genes.add(possible_genes);
				}
				
				//CALCULATE SCORE OF ALL POSSIBLE PERMUTATIONS----------------------
				ChromosomeP2 aux_chromosome = (ChromosomeP2) poblation[i].getCopy();
				double best_score = Integer.MAX_VALUE; // MAXIMIZE, MINIM?????????????????????????????????????????
				
				for (int p = 0; p < all_possible_genes.size(); p++) {
					aux_chromosome.setGenes(all_possible_genes.get(p));
					aux_chromosome.calculateFenotypes(); // NO SE SI ES NECESARIO HACERLO CADA VEZ QUE HACES SET GENES
					double brute_fitness = aux_chromosome.evaluate();
					
					// UPDATE CHROMOSOME WITH BEST FOUND------------------------------
					if(brute_fitness < best_score) {
						best_score = brute_fitness;
						chromosome.setGenes(aux_chromosome.getGenesCopy());
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
	public static List<int[]> CalculatePermutations(int[] numbers) {
		List<int[]> permutations = new ArrayList<int[]>();
		
	    // RETURN CURRENT ELEMENT BY DEFAULT
	    if (numbers.length == 1) {
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
}
