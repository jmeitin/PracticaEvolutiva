package CrossAlgorithms;

import java.util.Random;

import Chromosomes.ChromosomeP1F4b;
import GeneticAlgorithm.Chromosome;

public class BLXAlphaCross extends CrossAlgorithm {
	@Override
	protected void cross(Chromosome childA, Chromosome childB) {
		//CASTING FOR 4B------------------------------------------------------------------------------------------
		ChromosomeP1F4b child_a_b = (ChromosomeP1F4b)childA;
		ChromosomeP1F4b child_b_b = (ChromosomeP1F4b)childB;
		
		int num_genes = childA.getLenght();
		int num_alleles = childA.getAlleleLength();
		
		for (int g = 0; g < num_genes; g++) {
			for (int a = 0; a < num_alleles; a++) {
				double a1 = child_a_b.getGene(g).getAllele(a);
				double a2 = child_b_b.getGene(g).getAllele(a);
				
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
					
					double r = Math.random();
					double val_a = min_allele + r * (max_allele - min_allele);
					double val_b = min_allele + (1 - r) * (max_allele - min_allele);
					
					childA.getGene(g).setAllele(a, val_a);
					childB.getGene(g).setAllele(a, val_b);
				}
			}
		}
		
	}
}
