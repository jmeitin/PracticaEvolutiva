/**
 * 
 */
package MutationAlgorithm;

import java.util.Random;

import GeneticAlgorithm.Chromosome;

/**
 * @author Rioni
 *
 */
public class BasicGenMutation extends MutationAlgorithm {
	@Override
	public Chromosome[] mutate(Chromosome[] poblation, int poblation_size, double mutation_chance){
		// System.out.println("Mutate");
		Chromosome[] new_population = new Chromosome[poblation_size];
		Random rand = new Random();
		
		for (int i = 0; i < poblation_size; i++) {
			new_population[i] = poblation[i].getCopy();
			
			new_population[i].mutate(rand, mutation_chance);
		}

		return new_population;	
	}

}
