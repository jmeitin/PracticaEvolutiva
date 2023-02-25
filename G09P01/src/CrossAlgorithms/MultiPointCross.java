package CrossAlgorithms;

import java.util.List;
import java.util.Random;

import GeneticAlgorithm.Chromosome;

public class MultiPointCross extends CrossAlgorithm {

	@Override
	public Chromosome[] cross(Chromosome[] poblation, int poblation_size, double cross_chance, int numPoints) {
		//x(v) = xMin + bin2dec(v) * (xMax - xMin) / (2^lengthChromosome - 1)
		
		Chromosome[] new_population = new Chromosome[poblation_size];
		Random rand = new Random();
		
		for (int i = 0; i < poblation_size; i++) {
			double chance = rand.nextDouble(); // [0, 1]
			
			//CROSS HAPPENS
			if(chance <= cross_chance && i < poblation_size - 1) {
				
				Chromosome parentA = poblation[i];
				Chromosome parentB = poblation[i+1];

				int point = 1;
				
//				// numPOints = 3
//				// Chromosome = 0000 1111 1010
//				// point1 = 0000, point2 = 1111, point 3 = 1010
//				// num_genes = 12
//				for(genes in chromosome) {
//					// ADVANCE TO NEXT POINT
//					// gene = value from [0, 11]
//					// if 4 >= 1 / 12 ==> point = 2
//					if(gene > point/num_genes) {
//						point ++;
//						chance = rand.nextDouble();
//					}			
//					
//					if(chance <= cross_chance) {
//						ARRIBA
//					}
//					else {
//						ABAJO
//					}
//				}
				
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