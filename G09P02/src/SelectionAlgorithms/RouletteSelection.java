package SelectionAlgorithms;

import Chromosomes.Chromosome;
/*
 *  Escoge un punto aleatorio en la población, esto avanza mientras el punto aleatorio sea superior a la puntuación acumulada del cromosoma. 
 *  Una vez se encuentra un cromosoma válido se clona. Este proceso se repite hasta completar la población.
*/

public class RouletteSelection extends SelectionAlgorithm {

	@Override
	public Chromosome[] select(Chromosome[] poblation, int poblation_size) {

		Chromosome[] new_population = new Chromosome[poblation_size];
		for (int i = 0; i < poblation_size; i++) {
			double random_point =  Math.random() * Math.random();
			int pos_super = 0;
			
			while(pos_super < poblation_size && random_point > poblation[pos_super].getAccumulatedScore())
				pos_super++;
			
			new_population[i] = poblation[pos_super].getCopy();
		}

		return new_population;
	}
}
