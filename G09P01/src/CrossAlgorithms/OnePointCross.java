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
		
		for (int i = 0; i < poblation_size; i++) {
			double chance = rand.nextDouble(); // [0, 1]
			
			//CROSS HAPPENS
			if(chance <= cross_chance && i < poblation_size - 1) {
				
				Chromosome childA = poblation[i].getCopy();
				Chromosome childB = poblation[i+1].getCopy();
				
				//Point belongs to [1, length - 1]
				// if length = 10 & point = 0.3 ==> 1 + 0.3 * (10 - 2) = 2.7 ==> 2nd gene
				// if point 1.0 ==> 1 + 1 * 8 = 9 ==> 9th gene
				int length = childA.getLenght();
				double aux = 1 + rand.nextDouble() * (length - 2); 
				int point = (int)aux;
				
				for (int g = 0; g < length; g++) {
					if (g >= point) {
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
				new_population[i] = poblation[i];
			}
			
		}

		return new_population;		
	}
}