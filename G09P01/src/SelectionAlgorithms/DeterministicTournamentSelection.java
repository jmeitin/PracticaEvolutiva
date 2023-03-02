/**
 * 
 */
package SelectionAlgorithms;

import java.util.Random;

import Exceptions.NotImplementedException;
import GeneticAlgorithm.Chromosome;

/**
 * @author Rioni
 *
 */
public class DeterministicTournamentSelection extends SelectionAlgorithm {

	@Override
	public Chromosome[] select(Chromosome[] poblation, int poblation_size) {
		//CREATE NEW POPULATION
		Chromosome[] new_population = new Chromosome[poblation_size];
		Random rand = new Random();
		int num_people = poblation.length - 1;
		
		for (int i = 0; i < poblation_size; i ++) {
			// SELECT 3 RANDOM SPECIMENS. KEEP THE BEST ONE
			int a = (int)(rand.nextDouble() * num_people);
			int b = (int)(rand.nextDouble() * num_people);
			int c = (int)(rand.nextDouble() * num_people);
			
			int best = a;
			if(poblation[b].compareTo(poblation[a]) >= 0 && poblation[b].compareTo(poblation[c]) >= 0)
				best = b;
			else if (poblation[c].compareTo(poblation[a]) >= 0 && poblation[c].compareTo(poblation[b]) >= 0)
				best = c;
			
			new_population[i] = poblation[best].getCopy();
		}
		
		return new_population;
	}

}
