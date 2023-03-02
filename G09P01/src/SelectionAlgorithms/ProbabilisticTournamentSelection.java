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
public class ProbabilisticTournamentSelection extends SelectionAlgorithm {

	static double p = 0.5; //p = [0.5, 1.0]
	
	public void setP(int value) {p = value;}
	
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
					
					int selected = a;
					if(rand.nextDouble() > p) {
						int best = selected;
						if(poblation[b].getScore() >= poblation[a].getScore() && poblation[b].getScore() >= poblation[c].getScore())
							best = b;
						else if (poblation[c].getScore() >= poblation[a].getScore() && poblation[c].getScore() >= poblation[b].getScore())
							best = c;
						
						selected = best;
					}
					else {
						int worst = selected;
						if(poblation[b].getScore() <= poblation[a].getScore() && poblation[b].getScore() <= poblation[c].getScore())
							worst = b;
						else if (poblation[c].getScore() <= poblation[a].getScore() && poblation[c].getScore() <= poblation[b].getScore())
							worst = c;
						
						selected = worst; 
					}					
					
					new_population[i] = poblation[selected].getCopy();
				}
				
				return new_population;
	}

}
