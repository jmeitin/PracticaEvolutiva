/**
 * 
 */
package SelectionAlgorithms;

import java.util.Arrays;
import java.util.Random;

import Exceptions.NotImplementedException;
import GeneticAlgorithm.Chromosome;

/**
 * @author Rioni
 *
 */
public class StochasticSelection extends SelectionAlgorithm {

	@Override
	public Chromosome[] select(Chromosome[] poblation, int poblation_size) {

		Chromosome[] new_population = new Chromosome[poblation_size];
		final double distance = 1.0 / poblation_size;
		double random_point = Math.random() * distance;

		for (int i = 0; i < poblation_size; i++) {

			int pos_super = 0;
			while (pos_super < poblation_size && random_point > poblation[pos_super].getAccumulatedScore())
				pos_super++;

			pos_super = Math.min(pos_super, poblation_size -1); // Clamping
			
			new_population[i] = poblation[pos_super].getCopy();
			random_point += distance;
		}
		
		return new_population;
	}

}
