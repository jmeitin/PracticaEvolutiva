/**
 * 
 */
package SelectionAlgorithms;

import java.util.Random;

import Chromosomes.Chromosome;

/**
 * @author Rioni
 *
 */
public class RemainderSelection extends SelectionAlgorithm {
	// k = poblation_size
	
	@Override
	public Chromosome[] select(Chromosome[] poblation, int poblation_size) {
		
		Chromosome[] new_population = new Chromosome[poblation_size];
		Random rand = new Random();
		
		int index = 0;
		for (int i = 0; i < poblation_size && index < poblation_size; i ++) {
			int pos = (int)(rand.nextDouble() * (poblation.length - 1));			
			int pi_k = (int)(poblation[pos].getScore() * poblation_size);
			
			for (int j = 0; j < pi_k && index < poblation_size; j++) {
				if(index < poblation_size) {
					new_population[index] = poblation[pos];
					index += 1;
				}
			}
		}
		
		//Fill Array however you like		
		for (int i = index; i < poblation_size; i++) {
			int pos = (int)(rand.nextDouble() * (poblation.length - 1));
			new_population[i] = poblation[pos];
		}
		
		
		return new_population;
	}

}
