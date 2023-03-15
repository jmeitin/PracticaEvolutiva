/**
 * 
 */
package MutationAlgorithm;

import Chromosomes.Chromosome;

/**
 * @author Rioni
 *
 */
public class BasicGenMutation extends MutationAlgorithm {
	/**
	 *  Mutates chromosome by bit
	 */
	@Override
	public Chromosome[] mutate(Chromosome[] poblation, int poblation_size, double mutation_chance){
		Chromosome[] new_population = new Chromosome[poblation_size];
		
		for (int i = 0; i < poblation_size; i++) {
			new_population[i] = poblation[i].getCopy();
			
			new_population[i].mutate(rand, mutation_chance);
		}

		return new_population;	
	}

}
