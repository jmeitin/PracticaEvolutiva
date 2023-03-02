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
		Arrays.sort(poblation);
		
		//CALCULATE TOTAL FITNESS
		int fitness_sum = 0;
		for (int i = 0; i < poblation.length; i++) {
			fitness_sum += poblation[i].getFitness();
		}
		
		//CALCULATE ACCUMULATED SCORE
		int accumulated_score = 0;
		for (int i = 0; i < poblation.length; i++) {
			// Pi = fitness / fitness_sum
			poblation[i].setScore(poblation[i].getFitness() / fitness_sum);
			accumulated_score += poblation[i].getScore();
			poblation[i].setAccumulatedScore(accumulated_score);
		}
		
		//CREATE NEW POPULATION
		Chromosome[] new_population = new Chromosome[poblation_size];
		Random rand = new Random();
		double length_segment = 1 / poblation_size;
		double r = rand.nextDouble() * length_segment;
		
		int individuo = 0;
		for (int i = 0; i < poblation_size; i ++) {
			double aj = (r + i) / poblation_size; //0.183
			
			// individuo0 = 0.18 < 0.183 < 0.34 = individuo1
			while (poblation[individuo].getAccumulatedScore() < aj) {
				individuo++;
			}
			new_population[i] = poblation[individuo].getCopy();
		}
		
		return new_population;

	}

}
