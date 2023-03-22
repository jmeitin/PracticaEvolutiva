package CrossAlgorithms.P1;

import Chromosomes.Chromosome;
import CrossAlgorithms.CrossAlgorithm;

// LO HACE A NIVEL DE GEN, NO ALELOS

/*
 * Se seleccionan varios puntos de corte en el cromosoma y se intercambian los trozos de manera intercalada.
 */

public class MultiPointCross extends CrossAlgorithm {
	private int numPoints;
	int part = 1;
	
	@Override
	public Chromosome[] cross(Chromosome[] poblation, int poblation_size, double cross_chance, int num_points)
	{
		this.numPoints = num_points;
		part = 1;
		return super.cross(poblation, poblation_size, cross_chance, num_points);
	}
	
	@Override
	protected void cross(Chromosome first_child, Chromosome second_child) {
		int length = first_child.getNumOfGenes();
		// Calculate 1st Point that delimits 2 portions
		int point = calculateNextPoint(rand, 1, (length - 1) / numPoints);
		
		//CROSS GENES 
		// numPOints = 3
		// Chromosome = 0000 1111 1010
		// point1 = 0000, point2 = 1111, point 3 = 1010
		// num_genes = 12
		Boolean swap = false;
		for (int g = point; g < length; g++) {
			
			if (swap) { //swap genes by default
				first_child.swapGene(g, second_child);
			}
			//else stays the same
			
			// WE ARE IN THE LAST GENE OF THIS PORTION
		    if(g + 1 == point) { 
		    	// don't swap genes in next portion if you were already exchanging them
				if(swap) swap = false;
				// swap genes in next portion if you weren't exchanging them previously
				else swap = true;
				part++;
				// Calculate NEXT Point that delimits 2 portions
				point = calculateNextPoint(rand, point, part * (length - 1) / numPoints);
			}
		}
	}
}