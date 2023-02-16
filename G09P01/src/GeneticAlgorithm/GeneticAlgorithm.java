package GeneticAlgorithm;

import SelectionAlgorithms.SelectionAlgorithm;

public class GeneticAlgorithm<T, U> {
	private Chromosome<T, U>[] poblation; // clase individuo?
	private double[] fitness;// NICO NO LO USA?

	// Probabilities-------
	final private double cross_chance;
	final private double mutation_chance;

	// TAMANYOS------------
	final private int max_gen_num; // max generaciones
	final private int poblation_size;
	final private int tournament_size; // NICO NO LO USA?

	private Chromosome<T, U> best_chromosome;
	private int best_pos;

	final double tolerance;
	double best_absolute_fitness;
	final boolean maximize;
	final int dimensions;

	// Strategy
	final ChromosomeFactory<T, U> chromosomeFactory;
	final SelectionAlgorithm selectionAlgorithm;

	public GeneticAlgorithm(GeneticAlgorithmData algorithmData) {
		// POBLACION
		this.poblation = new Chromosome[algorithmData.poblation_size];
		// TAMANYOS
		this.poblation_size = algorithmData.poblation_size;
		this.max_gen_num = algorithmData.max_gen_num;
		// PROBABILIDADES
		this.cross_chance = algorithmData.cross_chance;
		this.mutation_chance = algorithmData.mutation_chance;
		this.tolerance = algorithmData.tolerance;
		this.maximize = algorithmData.maximize;
		this.best_absolute_fitness = algorithmData.maximize ? Double.MIN_VALUE : Double.MAX_VALUE;
		this.dimensions = algorithmData.dimensions;
		this.chromosomeFactory = algorithmData.chromosomeFactory;
		this.selectionAlgorithm = algorithmData.selectionAlgorithm;
		this.tournament_size = algorithmData.tournament_size;

	}

	public void initializePoblation() {
		for (int i = 0; i < poblation_size; i++)
			poblation[i] = createChromosome();
	}

	// Shorthand to ChromosomeFactory
	private Chromosome<T, U> createChromosome() {
		return chromosomeFactory.createChromosome(tolerance, dimensions);
	}

	public void run() {
		System.out.println("Run GeneticAlgorithm");
		initializePoblation();
		evaluate();

		for (int i = 0; i < this.max_gen_num; i++) {
			if (true) // elitism select
			{

			}

			select();
			cross();
			mutate();

			evaluate();
		}

		// Print fenotypes of best chromosome
		System.out.println("Best chromosome fenotypes:");
		for (U fenotype : best_chromosome.getFenotypes())
			System.out.println(fenotype);
	}

	public void select() {
		this.poblation = this.selectionAlgorithm.select(poblation, poblation_size);
	}

	public void cross() {

	}

	public void mutate() {

	}

	public void evaluate() {
		double accumulated_score = 0;
		double best_fitness = maximize ? Double.MIN_VALUE : Double.MAX_VALUE;
		double brute_fitness_sum = 0;

		for (int i = 0; i < poblation_size; i++) {
			double brute_fitness = poblation[i].evaluate();
			brute_fitness_sum += brute_fitness;
			if (compareFitness(brute_fitness, best_fitness))
			{
				best_fitness = brute_fitness;
				best_pos = i;
			}

			poblation[i].setScore(poblation[i].getFitness() / brute_fitness_sum);
			accumulated_score += poblation[i].getScore();
			poblation[i].setAccumulatedScore(accumulated_score);
		}

		this.best_chromosome = poblation[best_pos].getCopy();
		if (compareFitness(best_fitness, best_absolute_fitness))
			this.best_absolute_fitness = best_fitness;
	}
	
	private boolean compareFitness(double best_fitness, double other)
	{
		if(maximize)
			return best_fitness > other;
		else
			return best_fitness < other;
	}
}
