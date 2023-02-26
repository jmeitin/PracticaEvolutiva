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
		
		for (int i = 0; i < poblation_size; i++) {
			double chance = rand.nextDouble(); // [0, 1]
			
			//CROSS HAPPENS
			if(chance <= cross_chance && i < poblation_size - 1) {
				
				Chromosome childA = poblation[i].getCopy();
				Chromosome childB = poblation[i+1].getCopy();
				
				for (int g = 0; g < childA.getLenght(); g++) {
					double exchange = rand.nextDouble(); // [0, 1]
					
					if (exchange < 0.5) {
						childA.swapGene(g, childB);
					}
					//else stays the same
				}
				
				new_population[i] = childA;
				i++;
				new_population[i] = childB;		
			}
			
			//CROSS DOESN'T HAPPEN
			else {
				new_population[i] = poblation[i].getCopy();
			}
			
		}

		return new_population;	
	}
}