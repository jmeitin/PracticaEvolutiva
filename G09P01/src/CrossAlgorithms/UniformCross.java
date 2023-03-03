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
		
		int parent_selected = -1; // 1st parent hasn't been selected yet
		for (int i = 0; i < poblation_size; i++) {
			double chance = rand.nextDouble(); // [0, 1]
			
			//CROSS HAPPENS
			if(chance <= cross_chance) {
				// THERE IS NO 1st PARENT
				if (parent_selected == - 1) { 
					parent_selected = i;
					new_population[parent_selected] = poblation[parent_selected].getCopy(); //default value
				}
				//SELECT 2nd PARENT
				else { 
					Chromosome childA = poblation[parent_selected].getCopy();
					Chromosome childB = poblation[i].getCopy();
					
					//CROSS EVERY PAIR OF GENES
					for (int g = 0; g < childA.getLenght(); g++) {
						childA.swapAllelesInGene(childB, g, 0, rand, 0.5); // Cross chance is (50%)
					}
					//UPDATE NEW POPULATION
					new_population[parent_selected] = childA;
					new_population[i] = childB;	
					parent_selected = -1;
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