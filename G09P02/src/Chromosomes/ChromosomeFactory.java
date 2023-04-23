package Chromosomes;

public interface ChromosomeFactory<T,U> {
	Chromosome<T,U> createChromosome(double tolerance, int dimensions, int chromosomeIndex, int poblation_size);
}
