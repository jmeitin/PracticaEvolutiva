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
	/**
	 * Boolean flag that indicates whether the genetic algorithm is configured to maximize or minimize the fitness function
	 * @value True if the algorithm is maximizing the fitness function, false if it is minimizing
	 */
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

	/**
	 * Runs the genetic algorithm. It prints the fenotypes of the best chromosomes and the result of evaluating this one.
	 */
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
		
		System.out.print("Evaluation: ");
		System.out.println(best_chromosome.evaluate());
	}

	public void select() {
		this.poblation = this.selectionAlgorithm.select(poblation, poblation_size);
	}

	public void cross() {

	}

	public void mutate() {

	}

	/**
	 * Evaluates each chromosome, updates its scores and selects the best one.
	 */
	public void evaluate() {
		double fitness_sum = recalculateFitness();
		double accumulated_score = 0;
		double best_fitness = maximize ? Double.MIN_VALUE : Double.MAX_VALUE;

		for (int i = 0; i < poblation_size; i++) {
			double brute_fitness = poblation[i].evaluate();
			if (compareFitness(brute_fitness, best_fitness))
			{
				best_fitness = brute_fitness;
				best_pos = i;
			}

			poblation[i].setScore(poblation[i].getFitness() / fitness_sum);
			accumulated_score += poblation[i].getScore();
			poblation[i].setAccumulatedScore(accumulated_score);
		}

		this.best_chromosome = poblation[best_pos].getCopy();
		if (compareFitness(best_fitness, best_absolute_fitness))
			this.best_absolute_fitness = best_fitness;
	}
	
	/**
	 * Recalculates the fitness of each chromosome and updates its score.
	 * The fitness of each chromosome is evaluated and the maximum or minimum value is determined
	 * (this demends on the class member maximize. 
	 * After that we use this value to adjust the fitness of each chromosome and make sure that all
	 * fitness values are positive
	 * @see GeneticAlgorithm#maximize
	 * @return The sum of all adjusted fitness values
	 */
	private double recalculateFitness()
	{
		double extreme_value = maximize ? Double.MIN_VALUE : Double.MAX_VALUE;
		for(int i = 0; i < poblation_size; i++)
		{
			double brute_fitness = poblation[i].evaluate();
			if(compareFitness(brute_fitness, extreme_value))
				extreme_value = brute_fitness;
		}

		double fitness_sum = 0;
		for(int i = 0; i < poblation_size; i++)
		{
			double adjusted_fitness = (extreme_value * (maximize ? 1 : -1)) + poblation[i].getBruteFitness();
			fitness_sum += adjusted_fitness;
			poblation[i].setFitness(adjusted_fitness);
		}

		return fitness_sum;
	}

	/**
	 * If the genetic algorithm is configured to maximize the fitness function, this function will
	 * return true if the first fitness value is greater than the second fitness value. If the genetic
	 * algorithm is configured to minimize the fitness function, this function will return true if the
	 * first fitness value is less than the second fitness value.
	 * 
	 * This method can be also used to compare any pair of double values being aware of the maximize value.
	 * 
	 * @param best_fitness The fitness value of the chromosome with the best fitness so far
	 * @param other The fitness value of the other chromosome to compare
	 * @see GeneticAlgorithm#maximize
	 * @return True if the first fitness value is better than the second fitness value based on the maximize value
	 */
	private boolean compareFitness(double best_fitness, double other)
	{
		if(maximize)
			return best_fitness > other;
		else
			return best_fitness < other;
	}
}
