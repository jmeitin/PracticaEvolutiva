package GeneticAlgorithm;

import SelectionAlgorithms.SelectionAlgorithm;
import CrossAlgorithms.CrossAlgorithm;
import MutationAlgorithm.MutationAlgorithm;

public class GeneticAlgorithm<T, U> {
	private Chromosome<T, U>[] poblation; // clase individuo?
	private double[] average_fitnesses;
	private double[] best_absolute_fitnesses;
	private double[] best_fitnesses;

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
	 * Boolean flag that indicates whether the genetic algorithm is configured to
	 * maximize or minimize the fitness function
	 * 
	 * @value True if the algorithm is maximizing the fitness function, false if it
	 *        is minimizing
	 */
	final boolean maximize;
	final int dimensions;

	// Strategy
	final ChromosomeFactory<T, U> chromosomeFactory;
	final SelectionAlgorithm selectionAlgorithm;
	final CrossAlgorithm crossAlgorithm;
	final MutationAlgorithm mutationAlgorithm;

	// External control
	private boolean isRunning = false;

	// Getters
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * @return the best_fitnesses
	 */
	public double[] getBestFitnesses() {
		return best_fitnesses;
	}

	/**
	 * @return the best_absolute_fitnesses
	 */
	public double[] getBestAbsoluteFitnesses() {
		return best_absolute_fitnesses;
	}

	/**
	 * @return the average_fitnesses
	 */
	public double[] getAverageFitnesses() {
		return average_fitnesses;
	}

	public double getBestFitness() {
		return this.best_absolute_fitness;
	}

	/**
	 * @return the best_chromosome
	 */
	public Chromosome<T, U> getBest_chromosome() {
		return best_chromosome;
	}

	/**
	 * @param best_chromosome the best_chromosome to set
	 */
	public void setBest_chromosome(Chromosome<T, U> best_chromosome) {
		this.best_chromosome = best_chromosome;
	}

	public GeneticAlgorithm(GeneticAlgorithmData algorithmData) {
		// Poblation
		this.poblation = new Chromosome[algorithmData.poblation_size];

		// Sizes
		this.poblation_size = algorithmData.poblation_size;
		this.max_gen_num = algorithmData.max_gen_num;
		this.tournament_size = algorithmData.tournament_size;

		// Chances
		this.cross_chance = algorithmData.cross_chance;
		this.mutation_chance = algorithmData.mutation_chance;
		this.tolerance = algorithmData.tolerance;
		this.maximize = algorithmData.maximize;
		this.best_absolute_fitness = algorithmData.maximize ? Double.MIN_VALUE : Double.MAX_VALUE;
		this.dimensions = algorithmData.dimensions;
		this.chromosomeFactory = algorithmData.chromosomeFactory;

		// Algorithms:
		this.selectionAlgorithm = algorithmData.selectionAlgorithm;
		this.crossAlgorithm = algorithmData.crossAlgorithm;
		this.mutationAlgorithm = algorithmData.mutationAlgorithm;

		// Stats
		average_fitnesses = new double[this.max_gen_num];
		best_absolute_fitnesses = new double[this.max_gen_num];
		best_fitnesses = new double[this.max_gen_num];
	}

	public void initializePoblation() {
		for (int i = 0; i < poblation_size; i++)
			poblation[i] = createChromosome();
	}

	// Shorthand to ChromosomeFactory
	private Chromosome<T, U> createChromosome() {
		return chromosomeFactory.createChromosome(tolerance, dimensions);
	}

	public void stop() {
		isRunning = false;
	}

	/**
	 * Runs the genetic algorithm. It prints the fenotypes of the best chromosomes
	 * and the result of evaluating this one.
	 */
	public void run() {
		System.out.println("Run GeneticAlgorithm");
		isRunning = true;
		initializePoblation();
		evaluate(0);

		for (int i = 0; i < this.max_gen_num; i++) { // generations
			if (true) // elitism select
			{

			}

			select();
			cross();
			mutate();

			evaluate(i);
		}

		isRunning = false;
	}

	/**
	 * Evaluates each chromosome, updates its scores and selects the best one.
	 */
	public void evaluate(int generation) {
		double fitness_sum = recalculateFitness();
		double accumulated_score = 0;
		double best_fitness = maximize ? Double.MIN_VALUE : Double.MAX_VALUE;

		for (int i = 0; i < poblation_size; i++) {
			double brute_fitness = poblation[i].evaluate();
			if (compareFitness(brute_fitness, best_fitness)) {
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
		
		// Gather stats
		this.average_fitnesses[generation] = fitness_sum / poblation_size;
		this.best_fitnesses[generation] = best_chromosome.fitness;
		this.best_absolute_fitnesses[generation] = best_absolute_fitness;
	}

	public void select() {
		this.poblation = this.selectionAlgorithm.select(poblation, poblation_size);
	}

	public void cross() { // HE PUESTO NUM_POINTS = 2 POR PONER ALGO. DEBERIA SER UNA VARIABLE
		this.poblation = this.crossAlgorithm.cross(poblation, poblation_size, cross_chance, 2);

	}

	public void mutate() {
		this.poblation = this.mutationAlgorithm.mutate(poblation,  poblation_size, mutation_chance);

	}

	/**
	 * Recalculates the fitness of each chromosome and updates its score. The
	 * fitness of each chromosome is evaluated and the maximum or minimum value is
	 * determined (this demends on the class member maximize. After that we use this
	 * value to adjust the fitness of each chromosome and make sure that all fitness
	 * values are positive
	 * 
	 * @see GeneticAlgorithm#maximize
	 * @return The sum of all adjusted fitness values
	 */
	private double recalculateFitness() {
		double extreme_value = maximize ? Double.MIN_VALUE : Double.MAX_VALUE;
		for (int i = 0; i < poblation_size; i++) {
			double brute_fitness = poblation[i].evaluate();
			if (compareFitness(brute_fitness, extreme_value))
				extreme_value = brute_fitness;
		}

		double fitness_sum = 0;
		for (int i = 0; i < poblation_size; i++) {
			double adjusted_fitness = (extreme_value * (maximize ? 1 : -1)) + poblation[i].getBruteFitness();
			fitness_sum += adjusted_fitness;
			poblation[i].setFitness(adjusted_fitness);
		}

		return fitness_sum;
	}

	/**
	 * If the genetic algorithm is configured to maximize the fitness function, this
	 * function will return true if the first fitness value is greater than the
	 * second fitness value. If the genetic algorithm is configured to minimize the
	 * fitness function, this function will return true if the first fitness value
	 * is less than the second fitness value.
	 * 
	 * This method can be also used to compare any pair of double values being aware
	 * of the maximize value.
	 * 
	 * @param best_fitness The fitness value of the chromosome with the best fitness
	 *                     so far
	 * @param other        The fitness value of the other chromosome to compare
	 * @see GeneticAlgorithm#maximize
	 * @return True if the first fitness value is better than the second fitness
	 *         value based on the maximize value
	 */
	private boolean compareFitness(double best_fitness, double other) {
		if (maximize)
			return best_fitness > other;
		else
			return best_fitness < other;
	}
}
