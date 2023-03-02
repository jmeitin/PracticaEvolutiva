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
public class RemainderSelection extends SelectionAlgorithm {

	static int k = 8;
	
	@Override
	public Chromosome[] select(Chromosome[] poblation, int poblation_size) {
		if (k > poblation.length)
			return poblation;
		
		Chromosome[] new_population = new Chromosome[poblation_size];
		Random rand = new Random();
		
		int index = 0;
		for (int i = 0; i < k && index < poblation_size; i ++) {
			int pos = (int)(rand.nextDouble() * poblation.length);			
			int pi_k = (int)(poblation[pos].getScore() * k);
			
			for (int j = 0; j < pi_k && index < poblation_size; j++) {
				if(index < poblation_size) {
					new_population[index] = poblation[pos];
					index += 1;
				}
			}
		}
		
		//Fill Array however you like		
		for (int i = index; i < poblation_size; i++) {
			int pos = (int)(rand.nextDouble() * poblation.length);
			new_population[i] = poblation[pos];
		}
		
		
		return new_population;
	}

}
