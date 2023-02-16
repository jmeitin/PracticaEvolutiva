package SelectionAlgorithms;

import java.util.List;
import java.util.Random;

import GeneticAlgorithm.Chromosome;

public class RouletteSelection extends SelectionAlgorithm {

	@Override
	public Chromosome[] select(Chromosome[] poblation, int poblation_size) {
		double total_sum = 0.0;
		for (Chromosome cromosoma : poblation) {
			total_sum += cromosoma.getAccumulatedScore();
		}

		Chromosome[] new_population = new Chromosome[poblation_size];
		Random rand = new Random();
		for (int i = 0; i < poblation_size; i++) {
			double random_point = rand.nextDouble() * total_sum;
			double partial_sum = 0.0;
			for (Chromosome cromosoma : poblation) {
				partial_sum += cromosoma.getAccumulatedScore();
				if (partial_sum > random_point) {
					new_population[i] = cromosoma.getCopy();
					break;
				}
			}
		}

		return new_population;
	}
}
