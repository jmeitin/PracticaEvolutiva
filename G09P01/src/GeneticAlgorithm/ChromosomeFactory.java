package GeneticAlgorithm;

public interface ChromosomeFactory<T,U> {
	Chromosome<T,U> createChromosome(double tolerance, int dimensions);
}