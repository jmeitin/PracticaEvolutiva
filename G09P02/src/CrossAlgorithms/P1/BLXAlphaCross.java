package CrossAlgorithms.P1;

import Chromosomes.Chromosome;
import Chromosomes.ChromosomeP1F4b;

public class BLXAlphaCross extends CrossAlgorithm {
	@Override
	protected void cross(Chromosome first_child, Chromosome second_child) {
		// CASTING FOR 4B------------------------------------------------------------------------------------------
		ChromosomeP1F4b child_a_b = (ChromosomeP1F4b)first_child;
		ChromosomeP1F4b child_b_b = (ChromosomeP1F4b)second_child;
		
		int num_genes = first_child.getNumOfGenes();
		int num_alleles = first_child.getNumAllelesInGene();
		
		// Genes in chromosome
		for (int g = 0; g < num_genes; g++) {
			// Alleles in chromosome
			for (int a = 0; a < num_alleles; a++) {
				double c1_i = child_a_b.getGene(g).getAllele(a);
				double c2_i = child_b_b.getGene(g).getAllele(a);
				
				// Determine the maximum and minimum values of the alleles.
				double c_max = Math.max(c1_i, c2_i);
				double c_min = Math.min(c1_i, c2_i);
				
				// If the alleles have the same value, set both children to that value.
				if(c_max == c_min) {
					first_child.getGene(g).setAllele(a, c_max);
					second_child.getGene(g).setAllele(a, c_max);
				}
				else {
					double alpha_value, min_allele, max_allele;
					
					// Set the value of the alpha parameter, which determines the range of the crossover.
					alpha_value = 0.5;
					
					// Calculate the minimum and maximum values of the crossover range.
					min_allele = c_min - alpha_value * (c_max - c_min);
					max_allele = c_max + alpha_value * (c_max - c_min);
					
					// Calculate the new values of the alleles for each child using the crossover range and the random number.
					// The way is calculated makes sure values are clamped
					// [Cmin - I * alpha, Cmax + I * alpha]
					double r = Math.random();
					double val_a = min_allele + r * (max_allele - min_allele);
					double val_b = min_allele + (1 - r) * (max_allele - min_allele);
					
					first_child.getGene(g).setAllele(a, val_a);
					second_child.getGene(g).setAllele(a, val_b);
				}
			}
		}
		
	}
}
