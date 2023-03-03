package CrossAlgorithms;

import java.util.List;
import java.util.Random;

import Chromosomes.ChromosomeP1F4b;
import GeneticAlgorithm.Chromosome;
import GeneticAlgorithm.Gene;

public class ArithmeticCross extends CrossAlgorithm {
//THIS ONLY WORKS IN 4B
	
	final static double alpha = 0.5;
	
	@Override
	public Chromosome[] cross(Chromosome[] poblation, int poblation_size, double cross_chance, int num_points) {
		System.out.print("ARITMETICO");
		
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
					//CASTING FOR 4B------------------------------------------------------------------------------------------
					ChromosomeP1F4b childA_4b = (ChromosomeP1F4b)poblation[parent_selected].getCopy();
					ChromosomeP1F4b childB_4b = (ChromosomeP1F4b)poblation[i].getCopy();
					
					int num_genes = childA_4b.getLenght();
					int num_alleles = childA_4b.getAlleleLength();
					
					for (int g = 0; g < num_genes; g++) {
						for (int a = 0; a < num_alleles; a++) {
							double a1 = childA_4b.getGene(g).getAllele(a);
							double a2 = childB_4b.getGene(g).getAllele(a);
							
							//hi = αp1i + (1-α)p2i
							childA_4b.getGene(g).setAllele(a, alpha * a1 + (alpha -1) * a2);
							childB_4b.getGene(g).setAllele(a, (alpha -1) * a1 + alpha * a2);
						}
					}
					//UPDATE NEW POPULATION
					new_population[parent_selected] = childA_4b;
					new_population[i] = childB_4b;	
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