package CrossAlgorithms;

import java.util.Random;

import Chromosomes.ChromosomeP1F4b;
import GeneticAlgorithm.Chromosome;

public class BLXAlphaCross extends CrossAlgorithm {

	@Override
	public Chromosome[] cross(Chromosome[] poblation, int poblation_size, double cross_chance, int num_points) {
		Chromosome[] new_population = new Chromosome[poblation_size];
		Random rand = new Random();
		
		int parent_selected = -1;
		for (int i = 0; i < poblation_size; i++) {
			double chance = rand.nextDouble();
			
			if(chance <= cross_chance) {
				if (parent_selected == - 1) { 
					parent_selected = i;
					new_population[parent_selected] = poblation[parent_selected].getCopy();
				}
				else { 
					ChromosomeP1F4b childA = (ChromosomeP1F4b)poblation[parent_selected].getCopy();
					ChromosomeP1F4b childB = (ChromosomeP1F4b)poblation[i].getCopy();
					
					int num_genes = childA.getLenght();
					int num_alleles = childA.getAlleleLength();
					
					for (int g = 0; g < num_genes; g++) {
						for (int a = 0; a < num_alleles; a++) {
							double a1 = childA.getGene(g).getAllele(a);
							double a2 = childB.getGene(g).getAllele(a);
							
							double max = Math.max(a1, a2);
							double min = Math.min(a1, a2);
							
							if(max == min) {
								childA.getGene(g).setAllele(a, max);
								childB.getGene(g).setAllele(a, max);
							}
							else {
								double alpha, alpha_value, min_allele, max_allele;
								alpha = 0.5;
								alpha_value = alpha;
								min_allele = min - alpha_value * (max - min);
								max_allele = max + alpha_value * (max - min);
								
								double r = rand.nextDouble();
								double val_a = min_allele + r * (max_allele - min_allele);
								double val_b = min_allele + (1 - r) * (max_allele - min_allele);
								
								childA.getGene(g).setAllele(a, val_a);
								childB.getGene(g).setAllele(a, val_b);
							}
						}
					}
					new_population[parent_selected] = childA;
					new_population[i] = childB;	
					parent_selected = -1;
				}
			}
			else {
				new_population[i] = poblation[i].getCopy();
			}
			
		}

		return new_population;		
	}
}
