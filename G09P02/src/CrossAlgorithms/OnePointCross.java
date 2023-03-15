package CrossAlgorithms;

import Chromosomes.Chromosome;

/*
 * Corta los 2 progenitores en una posición aleatoria e intercambia los respectivos segmentos. 
 */

public class OnePointCross extends CrossAlgorithm {
	@Override
	protected void cross(Chromosome first_child, Chromosome second_child) {
		int length = first_child.getLenght();
		int alleleLength = first_child.getAlleleLength();
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
			//CROSS ALL ALLELES STARTING AT POINT allele
			first_child.swapAllelesInGene(second_child, g, allele, rand, 2); // Cross chance is (200%)
			if (g == gene + 1)
				allele = 0; //will exchange all alleles in gene
		}
	}
}