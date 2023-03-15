package MutationAlgorithm;

import Chromosomes.Chromosome;

public class InversionMutation extends MutationAlgorithm {

	@Override
	public Chromosome[] mutate(Chromosome[] poblation, int poblation_size, double mutation_chance){
		Chromosome[] new_population = new Chromosome[poblation_size];
		
		for (int i = 0; i < poblation_size; i++) {
			new_population[i] = poblation[i].getCopy();
			
			//for(int )
		}

		return new_population;	
	}
	
	//int allelePoint = calculateNextPoint(rand, 1 * alleleLength, (length - 1) * alleleLength);
}
