package MutationAlgorithm.P2;

import java.util.Arrays;
import java.util.Random;

import Chromosomes.Chromosome;
import Chromosomes.ChromosomeP2;
import MutationAlgorithm.MutationAlgorithm;

/*se aplica con una determinada probabilidad.
 * Consiste en seleccionar 2 puntos del individuo al azar & invertir los elementos que hay entre dichos puntos. 
*/

public class InversionMutation extends MutationAlgorithm {

	@Override
	public Chromosome[] mutate(Chromosome[] poblation, int poblation_size, double mutation_chance){
		int num_genes = poblation[0].getNumOfGenes();
		if(num_genes < 4) 
			return poblation;
		
		Chromosome[] new_population = new Chromosome[poblation_size];
		
		for (int i = 0; i < poblation_size; i++) {
			ChromosomeP2 chromosome = (ChromosomeP2) poblation[i].getCopy();
			
			if(rand.nextDouble() < mutation_chance) {
				// SELECT CUT POINTS--------------------------------------
				int[] points = new int[2];
				points[0] = 1 + (int)(rand.nextDouble() * (num_genes -2));
				points[1] = points[0];
				while (points[1] == points[0])
					points[1] = 1 + (int)(rand.nextDouble() * (num_genes -2));
				Arrays.sort(points);
				
				//APPLY DEFAULT VALUE-------------------------------------
				int[] new_genes = chromosome.getGenesCopy();
				
				// SWAP INNER SEGMENT-------------------------------------
				int[] swap = new int[points[1] - points[0]];
				
				for (int g = 0; g < swap.length / 2; g++) {
					int first = points[0] + g;
					int last = points[1] - g -1;
					int aux = new_genes[first];
					new_genes[first] = new_genes[last];
					new_genes[last] = aux;
				}
				
				chromosome.setGenes(new_genes);				
			}
			
			new_population[i] = chromosome;
		}

		return new_population;	
	}
	
	//int allelePoint = calculateNextPoint(rand, 1 * alleleLength, (length - 1) * alleleLength);
}
