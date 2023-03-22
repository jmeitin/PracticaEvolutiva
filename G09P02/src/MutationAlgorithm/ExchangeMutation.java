package MutationAlgorithm;

import java.util.Arrays;
import java.util.Random;

import Chromosomes.Chromosome;
import Chromosomes.ChromosomeP2;
import Chromosomes.Gene;

//A NIVEL DE GEN

public class ExchangeMutation extends MutationAlgorithm{
	
	@Override
	public Chromosome[] mutate(Chromosome[] poblation, int poblation_size, double mutation_chance){	
		
		int num_genes = poblation[0].getNumOfGenes();
		
		Chromosome[] new_population = new Chromosome[poblation_size];
		
		Random rand = new Random();
		
		for (int i = 0; i < poblation_size; i++) {
			ChromosomeP2 chromosome = (ChromosomeP2) poblation[i].getCopy();
			
			// SELECT GENES--------------------------------------
			int pointA = (int)(rand.nextDouble() * (num_genes-1));
			int pointB = (int)(rand.nextDouble() * (num_genes-1));
			
			//EXCHANGE GENES------------------------------------
			int aux = chromosome.getGeneInt(pointA);
			chromosome.setGene(pointA, chromosome.getGeneInt(pointB));
			chromosome.setGene(pointB, aux);
			
			new_population[i] = chromosome;
		}

		return new_population;	
	}
	
}
