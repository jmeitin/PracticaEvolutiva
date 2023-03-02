package CrossAlgorithms;

import java.util.List;
import java.util.Random;

import GeneticAlgorithm.Chromosome;

// LO HACE A NIVEL DE GEN, NO ALELOS

public class MultiPointCross extends CrossAlgorithm {

	@Override
	public Chromosome[] cross(Chromosome[] poblation, int poblation_size, double cross_chance, int numPoints) {
		//x(v) = xMin + bin2dec(v) * (xMax - xMin) / (2^lengthChromosome - 1)
		
		Chromosome[] new_population = new Chromosome[poblation_size];
		Random rand = new Random();
		
		int part = 1; //count number of cross points 
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
					// Calculate 1st Point that delimits 2 portions
					int point = calculateNextPoint(rand, 1, (length - 1) / numPoints);
					
					//CROSS GENES 
					// numPOints = 3
					// Chromosome = 0000 1111 1010
					// point1 = 0000, point2 = 1111, point 3 = 1010
					// num_genes = 12
					Boolean swap = false;
					for (int g = point; g < length; g++) {
						
						if (swap) { //swap genes by default
							childA.swapGene(g, childB);
						}
						//else stays the same
						
						// WE ARE IN THE LAST GENE OF THIS PORTION
					    if(g + 1 == point) { 
					    	// don't swap genes in next portion if you were already exchanging them
							if(swap) swap = false;
							// swap genes in next portion if you weren't exchanging them previously
							else swap = true;
							part++;
							// Calculate NEXT Point that delimits 2 portions
							point = calculateNextPoint(rand, point, part * (length - 1) / numPoints);
						}
					}
					
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