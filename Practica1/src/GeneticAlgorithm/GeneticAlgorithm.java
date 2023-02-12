package GeneticAlgorithm;

public class GeneticAlgorithm<T, U> {
	Chromosome<T, U>[] poblation;
	int poblation_size;
	int max_gen_num;
	Chromosome<T, U> best_chromosome;
	int best_pos;
	double cross_chance;
	double mutation_chance;
	double tolerance;
	double best_absolute_fitness;
	boolean maximize;
	int dimensions;
	ChromosomeFactory<T, U> chromosomeFactory;

	public GeneticAlgorithm(int poblation_size, int max_gen_num, double cross_chance, double mutation_chance,
			double tolerance, boolean maximize, int dimensions, ChromosomeFactory<T, U> chromosomeFactory) {
		this.poblation_size = poblation_size;
		this.max_gen_num = max_gen_num;
		this.cross_chance = cross_chance;
		this.mutation_chance = mutation_chance;
		this.tolerance = tolerance;
		this.maximize = maximize;
		this.best_absolute_fitness = maximize ? Double.MIN_VALUE : Double.MAX_VALUE;
		this.dimensions = dimensions;
		this.chromosomeFactory = chromosomeFactory;
		this.poblation = new Chromosome[poblation_size];
	}

	public void initializePoblation() {
		for (int i = 0; i < poblation_size; i++)
			poblation[i] = createChromosome();
	}

	// Shorthand to ChromosomeFactory
	private Chromosome<T, U> createChromosome() {
		return chromosomeFactory.createChromosome(tolerance, dimensions);
	}
	
	public void run()
	{
		System.out.println("Practica 1 Ejecuto correctamentee");
	}
}
