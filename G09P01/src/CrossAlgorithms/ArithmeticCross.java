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
	protected void cross(Chromosome parent_a, Chromosome parent_b) {
		//CASTING FOR 4B------------------------------------------------------------------------------------------
		ChromosomeP1F4b childA_4b = (ChromosomeP1F4b)parent_a;
		ChromosomeP1F4b childB_4b = (ChromosomeP1F4b)parent_b;
		
		int num_genes = childA_4b.getLenght();
		int num_alleles = childA_4b.getAlleleLength();
		//GENES IN CHROMOSOME
		for (int g = 0; g < num_genes; g++) {
			//ALLELES IN CHROMOSOME
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