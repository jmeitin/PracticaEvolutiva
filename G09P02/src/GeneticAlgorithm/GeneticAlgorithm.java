package GeneticAlgorithm;

import java.util.Arrays;
import java.util.Random;

import Chromosomes.BinaryTree;
import Chromosomes.Chromosome;
import Chromosomes.ChromosomeFactory;
import Chromosomes.ChromosomeP3;
import CrossAlgorithms.CrossAlgorithm;
import MutationAlgorithm.MutationAlgorithm;
import SelectionAlgorithms.SelectionAlgorithm;

public class GeneticAlgorithm<T, U> {
	static public int TREE_DEPTH = 5;

	Random rand = new Random();
	private Chromosome<T, U>[] poblation;
	private double[] average_fitnesses;
	private double[] best_absolute_fitnesses;
	private double[] best_fitnesses;

	// Probabilities -------
	final private double cross_chance;
	final private double mutation_chance;

	// Sizes ------------
	final private int max_gen_num; // max generaciones
	final private int poblation_size;
	private int current_generation; // used in sequential run

	// Elitism
	final private boolean elitism;
	private Chromosome<T, U>[] elite_poblation;

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
	final ChromosomeFactory<T, U> chromosome_factory;
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
	
	/**
	 * @return the current_generation
	 */
	public int getCurrent_generation() {
		return current_generation;
	}

	/**
	 * @return the max_gen_num
	 */
	public int getMax_gen_num() {
		return max_gen_num;
	}

	// AlgorithmData is readonly, used to init the class
	public GeneticAlgorithm(final GeneticAlgorithmData algorithmData) {
		// Poblation
		this.poblation = new Chromosome[algorithmData.poblation_size];

		// Sizes
		this.poblation_size = algorithmData.poblation_size;
		this.max_gen_num = algorithmData.max_gen_num;

		// Chances
		this.cross_chance = algorithmData.cross_chance;
		this.mutation_chance = algorithmData.mutation_chance;
		this.tolerance = algorithmData.tolerance;
		this.elitism = algorithmData.elitism_percentage > 0.0001;
		if (this.elitism)
			this.elite_poblation = new Chromosome[Math
					.max((int) (poblation_size * algorithmData.elitism_percentage / 100.0), 1)];
		this.maximize = algorithmData.maximize;
		this.best_absolute_fitness = algorithmData.maximize ? Double.MIN_VALUE : Double.MAX_VALUE;
		this.dimensions = algorithmData.dimensions;
		this.chromosome_factory = algorithmData.chromosome_factory;

		// Algorithms:
		this.selectionAlgorithm = algorithmData.selection_algorithm;
		this.crossAlgorithm = algorithmData.cross_algorithm;
		this.mutationAlgorithm = algorithmData.mutation_algorithm;
		
		//BinaryTree
		TREE_DEPTH = algorithmData.tree_depth;

		// Stats
		average_fitnesses = new double[this.max_gen_num];
		best_absolute_fitnesses = new double[this.max_gen_num];
		best_fitnesses = new double[this.max_gen_num];

		current_generation = 0;
	}

	public void initializePoblation() {
		for (int i = 0; i < poblation_size; i++)
			poblation[i] = createChromosome(i);
	}

	// Shorthand to ChromosomeFactory
	private Chromosome<T, U> createChromosome(int i) {
		return chromosome_factory.createChromosome(tolerance, dimensions, i, poblation_size);
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
			if (elitism)
				selectElite();

			select();
			cross();
			mutate();

			if (elitism)
				insertElite();

			evaluate(i);
		}

		isRunning = false;
	}

	public boolean runSequentially() {
		if (!isRunning) {
			isRunning = true;
			initializePoblation();
			evaluate(0);
		} else {
			if (elitism)
				selectElite();

			select();
			cross();
			mutate();
			

			if (elitism)
				insertElite();

			// BLOATING 
			if (poblation[0] instanceof ChromosomeP3) {
				tarpeian();
			}
			evaluate(current_generation);
			
			current_generation++;

			if (current_generation == max_gen_num)
				isRunning = false;
		}

		return current_generation != max_gen_num;
	}

	public void tarpeian() {
		//actualizo la profundidad de cada nodo
		int sumatorio = 0;
		for (int i = 0; i < poblation.length; i++) {
			ChromosomeP3 c = (ChromosomeP3)poblation[i];
			c.penalize (false); //don't penalize by default
			BinaryTree tree = c.getTree();
			tree.updateDepth();			
			sumatorio += tree.getDepth();
		}
		
		double average_depth = sumatorio / poblation.length;
		
		//comparo el depth de cada uno
		for (int i = 0; i < poblation.length; i++) {
			ChromosomeP3 c = (ChromosomeP3)poblation[i];
			BinaryTree tree = c.getTree();
			// 50% de penalizarlo
			if(tree.getDepth() > average_depth && rand.nextDouble() > 0.5) {
				//SOBREESCRIBE EL ARBOL
				c.penalize(true); //too deep. Must penalize
			}
			else {
				//System.out.println("Sobrevive tarpeian");
			}
		}
		
	}
	public void selectElite() {
		Arrays.sort(poblation);

		for (int i = 0; i < elite_poblation.length; i++)
			elite_poblation[i] = poblation[i].getCopy();
	}

	public void insertElite() {
		for (int i = 0; i < elite_poblation.length; i++)
			poblation[i] = elite_poblation[i].getCopy();
	}

	/**
	 * Evaluates each chromosome, updates its scores and selects the best one.
	 */
	public void evaluate(int generation) {
		double fitness_sum_adjusted = recalculateFitness();
		double fitness_sum_brute = 0;
		double accumulated_score = 0;
		double best_fitness = maximize ? Double.MIN_VALUE : Double.MAX_VALUE;

		for (int i = 0; i < poblation_size; i++) {
			double brute_fitness = poblation[i].evaluate();
			fitness_sum_brute += brute_fitness;
			if (compareFitness(brute_fitness, best_fitness)) {
				best_fitness = brute_fitness;
				best_pos = i;
			}

			poblation[i].setScore(poblation[i].getFitness() / fitness_sum_adjusted);
			accumulated_score += poblation[i].getScore();
			poblation[i].setAccumulatedScore(accumulated_score);
		}

		if (compareFitness(best_fitness, best_absolute_fitness) || generation == 0) {
			this.best_absolute_fitness = best_fitness;
			this.best_chromosome = this.poblation[this.best_pos].getCopy();
		}

		// Gather stats
		this.average_fitnesses[generation] = fitness_sum_brute / poblation_size;
		this.best_fitnesses[generation] = best_fitness;
		this.best_absolute_fitnesses[generation] = best_absolute_fitness;
	}

	public void select() {
		this.poblation = this.selectionAlgorithm.select(poblation, poblation_size);
	}

	public void cross() { // HE PUESTO NUM_POINTS = 2 POR PONER ALGO. DEBERIA SER UNA VARIABLE
		this.poblation = this.crossAlgorithm.cross(poblation, poblation_size, cross_chance, 2);

	}

	public void mutate() {
		this.poblation = this.mutationAlgorithm.mutate(poblation, poblation_size, mutation_chance);

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
		double min_adjusted_fitness = Double.MAX_VALUE;
		for (int i = 0; i < poblation_size; i++) {
		    double adjusted_fitness = 0;
		    if (!maximize)
		    	// Minimizar
				// extrene_value = max
		        adjusted_fitness = extreme_value - poblation[i].getBruteFitness();
		    else
		    	// Maximizar
				// extrene_value = min
		        adjusted_fitness = poblation[i].getBruteFitness() - extreme_value;

		    if (adjusted_fitness < min_adjusted_fitness) {
		        min_adjusted_fitness = adjusted_fitness;
		    }

		    poblation[i].setFitness(adjusted_fitness);
		}

		// Ensure all fitness values are positive (aptitude displacement)
		for (int i = 0; i < poblation_size; i++) {
		    double adjusted_fitness = poblation[i].getFitness() - min_adjusted_fitness;
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
