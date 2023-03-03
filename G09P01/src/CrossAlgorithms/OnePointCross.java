package CrossAlgorithms;

import java.util.List;
import java.util.Random;

import GeneticAlgorithm.Chromosome;
import GeneticAlgorithm.Gene;

public class OnePointCross extends CrossAlgorithm {

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
					
					int length = childA.getLenght();
					int alleleLength = childA.getAlleleLength();
					// Calculate Point in [1, L-1] that delimits 2 portions
					int allelePoint = calculateNextPoint(rand, 1 * alleleLength, (length - 1) * alleleLength);
					// 4 genes, 2 bits each ==> 01 23 45 67
					// allelePoint = 5 
					// genes = 5 / 2bits = gene2
					int gene = allelePoint / alleleLength;
					// allele = 5 % 2bits = allele1
					int allele = allelePoint % alleleLength;
					
					//CROSS GENES ON 2ND PORTION (1st PORTION STAYS THE SAME)					
					for (int g = gene; g < length; g++) {
						childA.swapAllelesInGene(childB, g, allele, rand, 2); // Cross chance is (200%)
						if (g == gene + 1)
							allele = 0; //will exchange all alleles in gene
					}
					//UPDATE NEW POPULATION
					new_population[parent_selected] = childA;
					new_population[i] = childB;	
					parent_selected = -1; // select 1st parent for new couple next
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