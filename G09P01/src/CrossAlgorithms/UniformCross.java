package CrossAlgorithms;

import java.util.List;
import java.util.Random;

import GeneticAlgorithm.Chromosome;

public class UniformCross extends CrossAlgorithm {

	@Override
	public Chromosome[] cross(Chromosome[] poblation, int poblation_size, double cross_chance, int num_points) {		
		//x(v) = xMin + bin2dec(v) * (xMax - xMin) / (2^lengthChromosome - 1)
		
		Chromosome[] new_population = new Chromosome[poblation_size];
		Random rand = new Random();
		
		int a = -1; // 1st parent hasn't been selected yet
		for (int i = 0; i < poblation_size; i++) {
			double chance = rand.nextDouble(); // [0, 1]
			
			//CROSS HAPPENS
			if(chance <= cross_chance) {
				// THERE IS NO 1st PARENT
				if (a == - 1) { 
					a = i;
					new_population[a] = poblation[a].getCopy(); //default value
				}
				//SELECT 2nd PARENT
				else { 
					Chromosome childA = poblation[a].getCopy();
					Chromosome childB = poblation[i].getCopy();
					
					//CROSS EVERY PAIR OF GENES IF P < 0.5 
					for (int g = 0; g < childA.getLenght(); g++) {
						double exchange = rand.nextDouble(); // [0, 1]
						
						if (exchange < 0.5) {
							childA.swapGene(g, childB);
						}
						//else stays the same
					}
					//UPDATE NEW POPULATION
					new_population[a] = childA;
					new_population[i] = childB;	
					a = -1;
				}
			}
			
			
			//CROSS DOESN'T HAPPEN
			else {
				new_population[i] = poblation[i].getCopy();
			}
			
		}

		return new_population;	
	}
}