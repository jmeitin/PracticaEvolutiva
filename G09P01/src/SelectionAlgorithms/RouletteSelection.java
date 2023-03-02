package SelectionAlgorithms;

import java.util.List;
import java.util.Random;

import GeneticAlgorithm.Chromosome;

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
