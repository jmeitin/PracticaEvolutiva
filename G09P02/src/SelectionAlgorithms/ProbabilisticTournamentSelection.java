/**
 * 
 */
package SelectionAlgorithms;

import java.util.Random;

import Chromosomes.Chromosome;

/**
 * @author Rioni
 * Igual que el T-Probalilístico solo que en vez de quedarse siempre con el mejor, genera un número aleatorio. 
 * Dependiendo de su valor, se queda con el mejor o el peor. *
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
						int best = selected; // a
						if(poblation[b].compareTo(poblation[a]) >= 0 && poblation[b].compareTo(poblation[c]) >= 0)
							best = b;
						else if (poblation[c].compareTo(poblation[a]) >= 0 && poblation[c].compareTo(poblation[b]) >= 0)
							best = c;
						
						selected = best;
					}
					else {
						int worst = selected; // a
						if(poblation[b].compareTo(poblation[a]) <= 0 && poblation[b].compareTo(poblation[c]) <= 0)
							worst = b;
						else if (poblation[c].compareTo(poblation[a]) <= 0 && poblation[c].compareTo(poblation[b]) <= 0)
							worst = c;
						
						selected = worst; 
					}					
					
					new_population[i] = poblation[selected].getCopy();
				}
				
				return new_population;
	}

}
