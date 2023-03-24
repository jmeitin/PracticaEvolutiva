package CrossAlgorithms;

import java.util.Random;

import Chromosomes.Chromosome;
import Utils.RandomUtils;

public abstract class CrossAlgorithm {
	
	protected Random rand = new Random();
	
	//ABSTRACT METHODS---------------
	protected abstract void cross(Chromosome first_child, Chromosome second_child);
	
	//METHODS---------------------
	public Chromosome[] cross(Chromosome[] poblation, int poblation_size, double cross_chance, int num_points)
	{
		Chromosome[] new_population = new Chromosome[poblation_size];
		
		int parent_selected = -1; // 1st parent hasn't been selected yet
		for (int i = 0; i < poblation_size; i++) {
			double chance = rand.nextDouble(); // [0, 1]
			
			// Cross happens
			if(RandomUtils.getProbability(cross_chance)) {
				// There is no first parent
				if (parent_selected == - 1) { 
					parent_selected = i;
					// In case no 2nd parent gets selected
					new_population[parent_selected] = poblation[parent_selected]; //default value
				}
				// Select 2nd parent
				else { 
					Chromosome parent_a = poblation[parent_selected].getCopy();
					Chromosome parent_b = poblation[i].getCopy();
					
					// Cross strategy
					cross(parent_a, parent_b);
					
					// Update population with the results
					new_population[parent_selected] = parent_a;
					new_population[i] = parent_b;	
					parent_selected = -1;
				}				
			}
			
			// Cross doesn't happen
			else {
				new_population[i] = poblation[i].getCopy();
			}
		}

		return new_population;		
	}
	
	// Divides genes
	protected int calculateNextPoint(Random rand, int start, int end) {
		// Point belongs to [1, length - 1]
		// if length = 10 & point = 0.3 ==> 1 + 0.3 * (10 - 2) = 2.7 ==> 2nd gene
		// if point 1.0 ==> 1 + 1 * 8 = 9 ==> 9th gene
		double aux = start + rand.nextDouble() * (end - start); 
		int point = (int)aux;
		
		return point;
	}
}
