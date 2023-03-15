package CrossAlgorithms;

import Chromosomes.Chromosome;
import Chromosomes.ChromosomeP1F4b;

/*
 * Realiza una combinación lineal entre los cromosomas de los padres. 
 * El caso más simple es el cruce de media aritmética:
 * hi = αp1i + (1-α)p2i
 * */
public class ArithmeticCross extends CrossAlgorithm {
//THIS ONLY WORKS IN 4B
	
	final static double alpha = 0.5;
	
	@Override
	protected void cross(Chromosome first_child, Chromosome second_child) {
		// CASTING FOR 4B------------------------------------------------------------------------------------------
		ChromosomeP1F4b childA_4b = (ChromosomeP1F4b)first_child;
		ChromosomeP1F4b childB_4b = (ChromosomeP1F4b)second_child;
		
		int num_genes = childA_4b.getLenght();
		int num_alleles = childA_4b.getAlleleLength();
		// Genes in chromosome
		for (int g = 0; g < num_genes; g++) {
			// Alleles in chromosome
			for (int a = 0; a < num_alleles; a++) {
				double a1 = childA_4b.getGene(g).getAllele(a);
				double a2 = childB_4b.getGene(g).getAllele(a);
				
				//hi = αp1i + (1-α)p2i
				childA_4b.getGene(g).setAllele(a, alpha * a1 + (alpha -1) * a2);
				childB_4b.getGene(g).setAllele(a, (alpha -1) * a1 + alpha * a2);
			}
		}
	}
}