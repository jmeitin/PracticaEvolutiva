package CrossAlgorithms;

import java.util.List;
import java.util.Random;

import GeneticAlgorithm.Chromosome;

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
				
				Chromosome parentA = poblation[i];
				Chromosome parentB = poblation[i+1];
				
				//chance = rand.nextDouble();
//				for(genes in chromosome) {
	//				if(gene < chance) {
	//					parte A
	//				}
	//				else {
	//					parte B
	//				}
	//			}
				
				new_population[i] = parentA;
				i++;
				new_population[i] = parentB;	
			}
			
			//CROSS DOESN'T HAPPEN
			else {
				new_population[i] = poblation[i];
			}
			
		}

		return new_population;		
	}
}