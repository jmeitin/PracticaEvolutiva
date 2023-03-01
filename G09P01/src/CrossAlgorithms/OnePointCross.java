package CrossAlgorithms;

import java.util.List;
import java.util.Random;

import GeneticAlgorithm.Chromosome;
import GeneticAlgorithm.Gene;

public class OnePointCross extends CrossAlgorithm {

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
					
					int length = childA.getLenght();
					// Calculate Point in [1, L-1] that delimits 2 portions
					int point = calculateNextPoint(rand, 1, length - 1);
					
					//CROSS GENES ON 2ND PORTION (1st PORTION STAYS THE SAME)
					for (int g = point; g < length; g++) {
						childA.swapGene(g, childB);
					}
					//UPDATE NEW POPULATION
					new_population[a] = childA;
					new_population[i] = childB;	
					a = -1; // select 1st parent for new couple next
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