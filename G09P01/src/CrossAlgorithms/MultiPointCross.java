package CrossAlgorithms;

import java.util.List;
import java.util.Random;

import GeneticAlgorithm.Chromosome;

//ASUME QUE LAS PARTICIONES SON DEL MISMO TAMANYO

public class MultiPointCross extends CrossAlgorithm {

	@Override
	public Chromosome[] cross(Chromosome[] poblation, int poblation_size, double cross_chance, int numPoints) {
		//x(v) = xMin + bin2dec(v) * (xMax - xMin) / (2^lengthChromosome - 1)
		
		Chromosome[] new_population = new Chromosome[poblation_size];
		Random rand = new Random();
		
		int part = 1;
		for (int i = 0; i < poblation_size; i++) {
			double chance = rand.nextDouble(); // [0, 1]
			
			//CROSS HAPPENS
			if(chance <= cross_chance && i < poblation_size - 1) {
				
				Chromosome childA = poblation[i].getCopy();
				Chromosome childB = poblation[i+1].getCopy();
				
				int length = childA.getLenght();
				int point = calculateNextPoint(rand, 1, (length - 1) / numPoints);
				
				// numPOints = 3
				// Chromosome = 0000 1111 1010
				// point1 = 0000, point2 = 1111, point 3 = 1010
				// num_genes = 12
				Boolean swap = true;
				for (int g = 0; g < length; g++) {
					
					if (swap) {
						childA.swapGene(g, childB);
					}
					//else stays the same
					
				    if(g + 1 == point) {
						if(swap) swap = false;
						else swap = true;
						part++;
						point = calculateNextPoint(rand, point, part * (length - 1) / numPoints);
					}
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