package View;

import java.text.DecimalFormat;

import Chromosomes.Chromosome;
import Chromosomes.ChromosomeFactory;
import Chromosomes.ChromosomeP1F1;
import Chromosomes.ChromosomeP1F2;
import Chromosomes.ChromosomeP1F3;
import Chromosomes.ChromosomeP1F4a;
import Chromosomes.ChromosomeP1F4b;
import CrossAlgorithms.ArithmeticCross;
import CrossAlgorithms.BLXAlphaCross;
import CrossAlgorithms.MultiPointCross;
import CrossAlgorithms.OnePointCross;
import CrossAlgorithms.UniformCross;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.GeneticAlgorithmData;
import MutationAlgorithm.BasicGenMutation;
import SelectionAlgorithms.DeterministicTournamentSelection;
import SelectionAlgorithms.ProbabilisticTournamentSelection;
import SelectionAlgorithms.RemainderSelection;
import SelectionAlgorithms.RouletteSelection;
import SelectionAlgorithms.StochasticSelection;
import SelectionAlgorithms.TruncationSelection;

public class ViewController implements Runnable {

	private class ModelRunner implements Runnable {
		@Override
		public void run() {
			geneticAlgorithm = new GeneticAlgorithm<Boolean, Double>(getAlgorithmData());
			geneticAlgorithm.run();
		}
	}

	private final boolean debugMode = true; // Enables debug mode
	private final MainView view; // View to be controlled
	private GeneticAlgorithm geneticAlgorithm; // Genetic algorithm to be run
	private GeneticAlgorithmData algorithmData; // Genetic algorithm data
	private Thread modelThread; // Thread that runs the genetic algorithm
	private Thread controllerRunThread; // Thread that runs the controller

	/**
	 * Shortcut to log a string if debug mode is enabled
	 * @param str String to be logged
	 */
	private void log(String str) {
		if (debugMode)
			System.out.println(str);
	}

	/**
	 * Constructor
	 * @param view View to be controlled
	 */
	public ViewController(final MainView view) {
		this.view = view;
		algorithmData = new GeneticAlgorithmData();
	}

	/**
	 * This thread will run the genetic algorithm and update the view once it's done
	 * It could also serve to track the progress of the algorithm and update the view accordingly (not implemented)
	 */ 
	private void runAux() {
		if (tryStopThread(modelThread)) // Cancel thread if already running
			geneticAlgorithm.stop();

		modelThread = new Thread(new ModelRunner());
		modelThread.start();

		while (modelThread.isAlive()) {
			// Here we can track the percentage of completion of the model
			// This runs in its own thread to not halt the UI
		}

		updateGraphsView();
		updateSolution();
		System.out.println("Thread end");
	}

	/**
	 * Runs the genetic algorithm in a separate thread to not halt the view.
	 * If the thread is already running, it will be stopped and a new one will be created
	 */
	public void run() {
		tryStopThread(controllerRunThread);

		controllerRunThread = new Thread(new Runnable() {
			public void run() {
				runAux();
			}
		});
		controllerRunThread.start();
	}

	/**
	 * Tries to stop a thread
	 * @param thread Thread to be stopped
	 * @return Wheter it could be stopped or not
	 */
	private boolean tryStopThread(Thread thread) {
		if (thread != null && thread.isAlive()) {
			thread.interrupt();
			System.out.println("Thread Stopped");
			return true;
		}

		return false;
	}

	/**
	 * Calls the view to update the graphs
	 */
	public void updateGraphsView() {
		if (geneticAlgorithm != null)
			view.updateGraph(this.geneticAlgorithm.getAverageFitnesses(),
					this.geneticAlgorithm.getBestAbsoluteFitnesses(), this.geneticAlgorithm.getBestFitnesses());
	}

	/**
	 * Returns a string given a double rounded to the given number of decimals
	 * @param number Number to be formatted
	 * @param num_decimals Number of decimals to be rounded
	 * @return
	 */
	private String formatNumber(double number, int num_decimals)
	{
		String format = "0.";
        for (int i = 0; i < num_decimals; i++) {
            format += "0";
        }
        DecimalFormat df = new DecimalFormat(format);
        return df.format(number);
	}
	
	/**
	 * Updates the solution text in the view
	 */
	private void updateSolution() {
		String solutionText = "";
		Chromosome chromosome = this.geneticAlgorithm.getBest_chromosome();
		int i = 1;
		for (Object fenotype : chromosome.getFenotypes()) {
			solutionText += "Variable X" + i++ + " = " + formatNumber((double)fenotype, 4) + " | ";
		}

		solutionText += "Valor de la función: " + formatNumber(chromosome.evaluate(), 4);

		this.view.setSolutionText(solutionText);
	}

	/**
	 * @return the algorithmData
	 */
	public GeneticAlgorithmData getAlgorithmData() {
		return algorithmData;
	}

	/**
	 * 
	 * @param poblation_size
	 */
	public void setPoblationSize(int poblation_size) {
		log("Poblation Size: " + poblation_size);
		this.algorithmData.poblation_size = poblation_size;
	}

	/**
	 * 
	 * @param generation_size
	 */
	public void setGenSize(int generation_size) {
		log("Generation Number: " + generation_size);
		this.algorithmData.max_gen_num = generation_size;
	}

	/**
	 * 
	 * @param elitism_percentage
	 */
	public void setElitism(double elitism_percentage) {
		log("Elitism chance: " + elitism_percentage);
		this.algorithmData.elitism_percentage = elitism_percentage;
	}

	/**
	 * Sets the tolerance of the algorithm (error value)
	 * @param tolerance Value between 0 and 1
	 */
	public void setTolerance(double tolerance) {
		log("Tolerance: " + tolerance);
		this.algorithmData.tolerance = tolerance;
	}

	/**
	 * Sets the selection algorithm
	 * @param function the function to set (P1 - FUNCION 1, P1 - FUNCION 2, P1 - FUNCION 3, P1 - FUNCION 4A, P1 - FUNCION 4B)
	 */
	public void setFunction(String function) {
		log("Function Type: " + function);
		ChromosomeFactory chromosome_factory;
		switch (function) {
		case "P1 - FUNCION 1":
			chromosome_factory = (double tolerance, int dimensions) -> {
				return new ChromosomeP1F1(2, tolerance);
			};
			algorithmData.chromosome_factory = chromosome_factory;
			algorithmData.maximize = true;
			break;
		case "P1 - FUNCION 2":
			chromosome_factory = (double tolerance, int dimensions) -> {
				return new ChromosomeP1F2(2, tolerance);
			};
			algorithmData.chromosome_factory = chromosome_factory;
			algorithmData.maximize = false;
			break;
		case "P1 - FUNCION 3":
			chromosome_factory = (double tolerance, int dimensions) -> {
				return new ChromosomeP1F3(2, tolerance);
			};
			algorithmData.chromosome_factory = chromosome_factory;
			algorithmData.maximize = false;
			break;
		case "P1 - FUNCION 4A":
			chromosome_factory = (double tolerance, int dimensions) -> {
				return new ChromosomeP1F4a(algorithmData.dimensions, tolerance);
			};
			algorithmData.chromosome_factory = chromosome_factory;
			algorithmData.maximize = false;
			break;
		case "P1 - FUNCION 4B":
			chromosome_factory = (double tolerance, int dimensions) -> {
				return new ChromosomeP1F4b(algorithmData.dimensions, tolerance);
			};
			algorithmData.chromosome_factory = chromosome_factory;
			algorithmData.maximize = false;
			break;
		}
	}

	/**
	 * Sets the selection algorithm to use
	 * @param selection It can be: RULETA, T-DETERMINÍSTICO, T-PROBABILÍSTICO, ESTOCÁSTICO, TRUNCAMIENTO, RESTOS
	 */
	public void setSelectionType(String selection) {
		log("Selection Type: " + selection);
		switch (selection) {
		case "RULETA":
			this.algorithmData.selection_algorithm = new RouletteSelection();
			break;
		case "T-DETERMINÍSTICO":
			this.algorithmData.selection_algorithm = new DeterministicTournamentSelection();
			break;
		case "T-PROBABILÍSTICO":
			this.algorithmData.selection_algorithm = new ProbabilisticTournamentSelection();
			break;
		case "ESTOCÁSTICO":
			this.algorithmData.selection_algorithm = new StochasticSelection();
			break;
		case "TRUNCAMIENTO":
			this.algorithmData.selection_algorithm = new TruncationSelection();
			break;
		case "RESTOS":
			this.algorithmData.selection_algorithm = new RemainderSelection();
			break;
		}
	}

	/**
	 * Sets the cross type
	 * @param cross Cross type (MONOPUNTO, UNIFORME, ARITMÉTICO, BLX-Α)
	 */
	public void setCrossType(String cross) {
		log("Cross Type: " + cross);
		switch (cross) {
		case "MONOPUNTO":
			this.algorithmData.cross_algorithm = new OnePointCross();
			break;
		case "MULTIPUNTO":
			this.algorithmData.cross_algorithm = new MultiPointCross();
			break;
		case "UNIFORME":
			this.algorithmData.cross_algorithm = new UniformCross();
			break;
		// Only for P1-F4a and P1-F4b
		case "ARITMÉTICO":
			this.algorithmData.cross_algorithm = new ArithmeticCross();
			break;
		case "BLX-Α":
			this.algorithmData.cross_algorithm = new BLXAlphaCross();
			break;
		}
	}

	/**
	 * Sets the mutation type
	 * @param mutationType Mutation type (BÁSICA)
	 */
	public void setMutationType(String mutationType) {
		log("Mutation Type: " + mutationType);
		switch (mutationType) {
		case "BÁSICA":
			this.algorithmData.mutation_algorithm = new BasicGenMutation();
			break;
		}
	}

	/**
	 * Sets the number of dimensions for the function 4a and 4b
	 * @param dimensions Number of dimensions
	 */
	public void setDimensions(int dimensions)
	{
		log("Dimensions: " + dimensions);
		algorithmData.dimensions = dimensions;
	}
	
	/**
	 * Sets cross chance
	 * @param cross_chance Cross chance in percentage (0 - 100%)
	 */
	public void setCrossChance(double cross_chance) {
		log("Cross chance: " + cross_chance);
		this.algorithmData.cross_chance = cross_chance;
	}

	/**
	 * Sets mutation chance
	 * @param mutation_chance Mutation chance in percentage (0 - 100%)
	 */
	public void setMutationChance(double mutation_chance) {
		log("Mutation chance: " + mutation_chance);
		this.algorithmData.mutation_chance = mutation_chance;
	}

}
