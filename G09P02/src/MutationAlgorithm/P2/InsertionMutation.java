package MutationAlgorithm.P2;

import java.util.ArrayList;
import java.util.List;

import Chromosomes.Chromosome;
import Chromosomes.ChromosomeP2;
import MutationAlgorithm.MutationAlgorithm;
import Utils.RandomUtils;

/*En este tipo de mutación se inserta una o varias ciudades elegidas al azar en unas posiciones también elegidas al azar.
 *  El caso más simple es con una sola inserción, por ejemplo, seleccionamos el alelo/gen 4 y lo insertamos en la tercera posición, tal y como se muestra.
*/

public class InsertionMutation extends MutationAlgorithm{
	
	protected static int  inserted_num = 1;
	
	@Override
	public Chromosome[] mutate(Chromosome[] poblation, int poblation_size, double mutation_chance){
		if (inserted_num > poblation_size) 
			inserted_num = 1;		
		
		int num_genes = poblation[0].getNumOfGenes();
		Chromosome[] new_population = new Chromosome[poblation_size];
		// WE USE DYNAMIC LISTS TO INSERT & REMOVE GENES
		List<Integer> old_genes = new ArrayList<Integer>();
		List<Integer> new_genes = new ArrayList<Integer>();
		
		for(int i = 0; i < poblation_size; i++) {
			ChromosomeP2 chromosome = (ChromosomeP2)poblation[i].getCopy();
			int [] genes = chromosome.getGenesCopy();
			
			for(int g = 0; g < num_genes; g++) {
				old_genes.add(genes[g]);
			}
			
			//MUTATE
			for(int g = 0; g < num_genes; g++) {
				int pos = 0;
				//INSERT
				if (RandomUtils.getProbability(mutation_chance)) {
					// Random number between 0 and max (both inclusive)
					pos = RandomUtils.getRandomInt(old_genes.size() - 1); 
				}
				
				new_genes.add(old_genes.get(pos));
				old_genes.remove(pos);
			}
			
			// UPDATE GENES
			for(int g = 0; g < num_genes; g++) {
				genes[g] = new_genes.get(0);
				new_genes.remove(0);
			}
			
			chromosome.setGenes(genes);
			new_population[i] = chromosome;
		}

		return new_population;	
	}
}
