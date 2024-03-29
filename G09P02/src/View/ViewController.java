package View;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import Chromosomes.Chromosome;
import Chromosomes.ChromosomeFactory;
import Chromosomes.ChromosomeP1F1;
import Chromosomes.ChromosomeP1F2;
import Chromosomes.ChromosomeP1F3;
import Chromosomes.ChromosomeP1F4a;
import Chromosomes.ChromosomeP1F4b;
import Chromosomes.ChromosomeP2;
import Chromosomes.ChromosomeP3;
import Chromosomes.ChromosomeP3opcional;
import CrossAlgorithms.P1.ArithmeticCross;
import CrossAlgorithms.P1.BLXAlphaCross;
import CrossAlgorithms.P1.MultiPointCross;
import CrossAlgorithms.P1.OnePointCross;
import CrossAlgorithms.P1.UniformCross;
import CrossAlgorithms.P2.COCross;
import CrossAlgorithms.P2.CXCross;
import CrossAlgorithms.P2.ERXCross;
import CrossAlgorithms.P2.OXCross;
import CrossAlgorithms.P2.OXPOCross;
import CrossAlgorithms.P2.OXPPCross;
import CrossAlgorithms.P2.OriginalCross;
import CrossAlgorithms.P2.PMXCross;
import CrossAlgorithms.P3.TreeCross;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.GeneticAlgorithmData;
import MutationAlgorithm.BasicGenMutation;
import MutationAlgorithm.P2.ExchangeMutation;
import MutationAlgorithm.P2.HeuristicMutation;
import MutationAlgorithm.P2.InsertionMutation;
import MutationAlgorithm.P2.InversionMutation;
import MutationAlgorithm.P2.OriginalMutation;
import MutationAlgorithm.P3.FunctionMutation;
import MutationAlgorithm.P3.TerminalMutation;
import MutationAlgorithm.P3.TreeSubTreeMutation;
import SelectionAlgorithms.DeterministicTournamentSelection;
import SelectionAlgorithms.ProbabilisticTournamentSelection;
import SelectionAlgorithms.RemainderSelection;
import SelectionAlgorithms.RouletteSelection;
import SelectionAlgorithms.StochasticSelection;
import SelectionAlgorithms.TruncationSelection;

public class ViewController implements Runnable {

	private class ModelRunner implements Runnable {
		private final GeneticAlgorithmData data;
		private boolean ranged = false;
		public GeneticAlgorithm algorithm = null;

		public int getCompletion() {
			if (algorithm == null)
				return 0;

			return (algorithm.getCurrent_generation() * 100 / algorithm.getMax_gen_num());
		}

		public GeneticAlgorithmData getDataCopy() {
			return data.getCopy();
		}

		public ModelRunner(int poblation_size, double cross_chance, double mutation_chance) {
			data = getFromRange(poblation_size, cross_chance, mutation_chance);
			ranged = true;
		}

		public ModelRunner() {
			data = getAlgorithmData();
		}

		@Override
		public void run() {
			algorithm = new GeneticAlgorithm<Boolean, Double>(data);

			log("Running: " + Thread.currentThread().getName());
			while (!Thread.interrupted() && algorithm.runSequentially()) {
			}

			if (!ranged)
				geneticAlgorithm = algorithm;
		}
	}

	private final boolean debugMode = false; // Enables debug mode
	private final MainView view; // View to be controlled
	private GeneticAlgorithm geneticAlgorithm; // Genetic algorithm to be run
	private GeneticAlgorithmData algorithmData; // Genetic algorithm data
	private Thread modelThread; // Thread that runs the genetic algorithm
	private Thread controllerRunThread; // Thread that runs the controller

	// Ranged values
	private boolean ranged_mode = false;
	private final int RANGED_STEPS = 5; // Num of tests for range
	private final int MAX_THREADS = 8; // Num of tests for range
	private RangedValue<Integer> poblation_size_range;
	private RangedValue<Double> cross_range;
	private RangedValue<Double> mutation_range;
	private ExecutorService executor;

	/**
	 * Shortcut to log a string if debug mode is enabled
	 * 
	 * @param str String to be logged
	 */
	private void log(String str) {
		if (debugMode)
			System.out.println(str);
	}

	/**
	 * Constructor
	 * 
	 * @param view View to be controlled
	 */
	public ViewController(final MainView view) {
		this.view = view;
		algorithmData = new GeneticAlgorithmData();
		poblation_size_range = new RangedValue(RANGED_STEPS);
		cross_range = new RangedValue(RANGED_STEPS);
		mutation_range = new RangedValue(RANGED_STEPS);
	}

	/**
	 * This thread will run the genetic algorithm and update the view once it's done
	 * It could also serve to track the progress of the algorithm and update the
	 * view accordingly (not implemented)
	 */
	private void runAux() {
		log("Running: " + Thread.currentThread().getName());
		if (!ranged_mode)
			runNormal();
		else
			runSlider();
	}

	private void runNormal() {
		ModelRunner model = new ModelRunner();
		modelThread = new Thread(model, "Model Thread");
		modelThread.start();

		boolean interrupted = false;
		while (modelThread.isAlive() && !interrupted) {
			// Here we can track the percentage of completion of the model
			// This runs in its own thread to not halt the UI
			// Calculate percentage of completion using getCurrent_generation and
			// getMax_gen_num
			view.setProgressBarPercentage(model.getCompletion());
			interrupted = Thread.interrupted();
		}

		view.enableDisableStopButton(false);

		if (interrupted) {
			view.setProgressBarPercentage(0);
			tryStopThread(modelThread);
		} else {
			view.setProgressBarPercentage(100);
			updateGraphsView();
			updateSolution();
		}
	}

	private void runSlider() {
		// Get the number of combinations to test
		final int num_combinations = poblation_size_range.getNumSteps() * cross_range.getNumSteps()
				* mutation_range.getNumSteps();
		ModelRunner[] models = new ModelRunner[num_combinations];
		executor = Executors.newFixedThreadPool(MAX_THREADS);

		int i = 0;
		for (int poblation_size : poblation_size_range) {
			for (double cross_chance : cross_range) {
				for (double mutation_chance : mutation_range) {
					// Acquire permit from semaphore
					models[i] = new ModelRunner(poblation_size, cross_chance, mutation_chance);
					executor.execute(models[i]);
					i++;
				}
			}
		}

		// No more threads will be added
		executor.shutdown();

		int progress = 0;
		while (!executor.isTerminated()) {
			progress = 0;
			for (int j = 0; j < num_combinations; j++)
				progress += models[j].getCompletion();

			// Update progress bar with 'progress' value
			progress /= num_combinations;
			view.setProgressBarPercentage(progress);

			if (Thread.interrupted()) {
				executor.shutdownNow();
				return;
			}
		}

		// We order the models using a stream to get the one with the biggest or lower
		// value
		// (depends on algorithmData.maximize field)
		List<ModelRunner> modelList = Arrays.asList(models);
		List<ModelRunner> sortedModels = modelList.stream().sorted((model1, model2) -> {
			Chromosome bestChromosome1 = model1.algorithm.getBest_chromosome();
			Chromosome bestChromosome2 = model2.algorithm.getBest_chromosome();
			double bestValue1 = bestChromosome1.evaluate();
			double bestValue2 = bestChromosome2.evaluate();
			return getAlgorithmData().maximize ? Double.compare(bestValue2, bestValue1)
					: Double.compare(bestValue1, bestValue2);
		}).collect(Collectors.toList());

		geneticAlgorithm = sortedModels.get(0).algorithm;
		GeneticAlgorithmData model = sortedModels.get(0).getDataCopy();
		// Print poblation size, cross chance and mutation chance of the best model
		log("Best model: " + model.poblation_size + " " + model.cross_chance + " " + model.mutation_chance);

		// Update view
		view.setProgressBarPercentage(100);
		view.enableDisableStopButton(false);
		updateGraphsView();
		updateSolution();
	}

	/**
	 * Runs the genetic algorithm in a separate thread to not halt the view. If the
	 * thread is already running, it will be stopped and a new one will be created
	 */
	public void run() {
		view.enableDisableStopButton(true);
		tryStopThread(controllerRunThread);

		controllerRunThread = new Thread(new Runnable() {
			public void run() {
				runAux();
			}
		}, "Controller Thread");
		controllerRunThread.start();
	}

	/**
	 * Tries to stop a thread
	 * 
	 * @param thread Thread to be stopped
	 * @return Wheter it could be stopped or not
	 */
	private boolean tryStopThread(Thread thread) {
		if (thread != null && thread.isAlive()) {
			log(thread.getName() + " have signaled to be interrupted");
			thread.interrupt();

			try {
				thread.join();
				log(thread.getName() + " interruption succeded");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
	 * 
	 * @param number       Number to be formatted
	 * @param num_decimals Number of decimals to be rounded
	 * @return
	 */
	private String formatNumber(double number, int num_decimals) {
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

		Chromosome chromosome = this.geneticAlgorithm.getBest_chromosome();
		// P1 function solution uses fenotypes. In P2 genotype and fenotype coincide,
		// but
		// we don`t want to write it like "Variable X+1", we want to write it like a
		// list
		// so we decided to do two similar methods. We know we could have done one
		// parametriced but this approach gives are
		// more flexibility in the future.
		if (chromosome instanceof ChromosomeP2)
			updateSolutionP2(chromosome);
		else if (chromosome instanceof ChromosomeP3)
			updateSolutionP3(chromosome);
		else if (chromosome instanceof ChromosomeP3opcional)
			updateSolutionP3optional(chromosome);
		else
			updateSolutionP1(chromosome);

	}

	private void updateSolutionP1(Chromosome chromosome) {
		String solutionText = "";
		int i = 1;
		for (Object fenotype : chromosome.getFenotypes()) {
			solutionText += "Variable X" + i++ + " = " + formatNumber((double) fenotype, 4) + " | ";
		}

		solutionText += "Valor de la función: " + formatNumber(chromosome.evaluate(), 4);

		this.view.setSolutionText(solutionText);
	}

	private void updateSolutionP2(Chromosome chromosome) {
		String solutionText = "Recorrido: [";
		for (Object fenotype : chromosome.getFenotypes()) {
			solutionText += fenotype + ", ";
		}

		solutionText = solutionText.substring(0, solutionText.length() - 2);
		solutionText += "] | Distancia: " + chromosome.evaluate();

		this.view.setSolutionText(solutionText);
	}

	private void updateSolutionP3(Chromosome chromosome) {
		ChromosomeP3 result = (ChromosomeP3) chromosome;
		this.view.setSolutionText(result.getFunctionString());
		this.view.plotGraph(result.getRealFunction(), result.getEstimatedFunction());
	}

	private void updateSolutionP3optional(Chromosome chromosome) {
		ChromosomeP3opcional result = (ChromosomeP3opcional) chromosome;
		this.view.setSolutionText(result.getFunctionString());
		this.view.plotGraph(result.getRealFunction(), result.getEstimatedFunction());
	}

	/**
	 * @return the algorithmData
	 */
	public GeneticAlgorithmData getAlgorithmData() {
		return algorithmData;
	}

	// Create new GeneticAlgorithmData with the current ranged data for the thread
	private GeneticAlgorithmData getFromRange(final int poblation_size, final double cross_chance,
			final double mutation_chance) {
		GeneticAlgorithmData data = this.algorithmData.getCopy();
		data.poblation_size = poblation_size;
		data.cross_chance = cross_chance;
		data.mutation_chance = mutation_chance;
		return data;
	}

	public void setSliderMode(boolean ranged_mode) {
		log("Slider mode: " + ranged_mode);
		this.ranged_mode = ranged_mode;
	}

	/**
	 * 
	 * @param poblation_size
	 */
	public void setPoblationSize(int poblation_size) {
		log("Poblation Size: " + poblation_size);
		this.algorithmData.poblation_size = poblation_size;
	}

	public void setPoblationSizeRange(int min, int max) {
		log("Poblation size range min-max: :" + min + ", " + max);
		this.poblation_size_range.min_value = min;
		this.poblation_size_range.max_value = max;
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
	 * 
	 * @param tolerance Value between 0 and 1
	 */
	public void setTolerance(double tolerance) {
		log("Tolerance: " + tolerance);
		this.algorithmData.tolerance = tolerance;
	}

	/**
	 * Sets the selection algorithm
	 * 
	 * @param function the function to set (P1 - FUNCION 1, P1 - FUNCION 2, P1 -
	 *                 FUNCION 3, P1 - FUNCION 4A, P1 - FUNCION 4B)
	 */
	public void setFunction(String function) {
		log("Function Type: " + function);
		ChromosomeFactory chromosome_factory;
		algorithmData.tree_depth = 0;
		switch (function) {
		case "P1 - FUNCION 1":
			chromosome_factory = (double tolerance, int dimensions, int index, int poblation_size) -> {
				return new ChromosomeP1F1(2, tolerance);
			};
			algorithmData.chromosome_factory = chromosome_factory;
			algorithmData.maximize = false;
			break;
		case "P1 - FUNCION 2":
			chromosome_factory = (double tolerance, int dimensions, int index, int poblation_size) -> {
				return new ChromosomeP1F2(2, tolerance);
			};
			algorithmData.chromosome_factory = chromosome_factory;
			algorithmData.maximize = false;
			break;
		case "P1 - FUNCION 3":
			chromosome_factory = (double tolerance, int dimensions, int index, int poblation_size) -> {
				return new ChromosomeP1F3(2, tolerance);
			};
			algorithmData.chromosome_factory = chromosome_factory;
			algorithmData.maximize = false;
			break;
		case "P1 - FUNCION 4A":
			chromosome_factory = (double tolerance, int dimensions, int index, int poblation_size) -> {
				return new ChromosomeP1F4a(algorithmData.dimensions, tolerance);
			};
			algorithmData.chromosome_factory = chromosome_factory;
			algorithmData.maximize = false;
			break;
		case "P1 - FUNCION 4B":
			chromosome_factory = (double tolerance, int dimensions, int index, int poblation_size) -> {
				return new ChromosomeP1F4b(algorithmData.dimensions, tolerance);
			};
			algorithmData.chromosome_factory = chromosome_factory;
			algorithmData.maximize = false;
			break;
		case "P2":
			chromosome_factory = (double tolerance, int dimensions, int index, int poblation_size) -> {
				return new ChromosomeP2(tolerance);
			};
			algorithmData.chromosome_factory = chromosome_factory;
			algorithmData.maximize = false;
			break;
		case "P3":
			chromosome_factory = (double tolerance, int dimensions, int index, int poblation_size) -> {
				final int maxDepth = 5;
				int depth = maxDepth;
				String inicializationType = algorithmData.inicializationType;

				if (inicializationType.equals("RAMPED AND HALF")) {

					final int group_size = poblation_size / (maxDepth - 1);
					final int half_group_size = group_size / 2;
					final int subgroupIndex = (index / group_size);
					final int half_subgroup_index = subgroupIndex * group_size + half_group_size;

					depth = subgroupIndex + 2;

					if (index < half_subgroup_index) {
						inicializationType = "COMPLETA";
					} else {
						inicializationType = "CRECIENTE";
					}
				}

				// GeneticAlgorithm.TREE_DEPTH = maxDepth;
				algorithmData.tree_depth = maxDepth;

				return new ChromosomeP3(tolerance, depth, inicializationType);
			};
			algorithmData.chromosome_factory = chromosome_factory;
			algorithmData.maximize = false;
			break;
		case "P3 - OPCIONAL":
			chromosome_factory = (double tolerance, int dimensions, int index, int poblation_size) -> {
				return new ChromosomeP3opcional(30, tolerance, dimensions);
			};
			algorithmData.chromosome_factory = chromosome_factory;
			algorithmData.maximize = false;
			break;
		}
	}

	/**
	 * Sets the selection algorithm to use
	 * 
	 * @param selection It can be: RULETA, T-DETERMINÍSTICO, T-PROBABILÍSTICO,
	 *                  ESTOCÁSTICO, TRUNCAMIENTO, RESTOS
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
	 * 
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
		case "PMX":
			this.algorithmData.cross_algorithm = new PMXCross();
			break;
		case "ORIGINAL":
			this.algorithmData.cross_algorithm = new OriginalCross();
			break;
		case "OX":
			this.algorithmData.cross_algorithm = new OXCross();
			break;
		case "OX-PP":
			this.algorithmData.cross_algorithm = new OXPPCross();
			break;
		case "OX-PO":
			this.algorithmData.cross_algorithm = new OXPOCross();
			break;
		case "CX":
			this.algorithmData.cross_algorithm = new CXCross();
			break;
		case "ERX":
			this.algorithmData.cross_algorithm = new ERXCross();
			break;
		case "CO":
			this.algorithmData.cross_algorithm = new COCross();
			break;
		case "INTERCAMBIO":
			this.algorithmData.cross_algorithm = new TreeCross();
			break;
		}
	}

	/**
	 * Sets the mutation type
	 * 
	 * @param mutationType Mutation type (BÁSICA)
	 */
	public void setMutationType(String mutationType) {
		log("Mutation Type: " + mutationType);

		// "Intercambio", "Inversion", "Insercion", "Heuristica"
		switch (mutationType) {
		case "BÁSICA":
			this.algorithmData.mutation_algorithm = new BasicGenMutation();
			break;
		case "INTERCAMBIO":
			this.algorithmData.mutation_algorithm = new ExchangeMutation();
			break;
		case "INVERSION":
			this.algorithmData.mutation_algorithm = new InversionMutation();
			break;
		case "INSERCION":
			this.algorithmData.mutation_algorithm = new InsertionMutation();
			break;
		case "HEURISTICA":
			this.algorithmData.mutation_algorithm = new HeuristicMutation();
			break;
		case "ORIGINAL":
			this.algorithmData.mutation_algorithm = new OriginalMutation();
			break;
		case "TERMINAL":
			this.algorithmData.mutation_algorithm = new TerminalMutation();
			break;
		case "SUBARBOL":
			this.algorithmData.mutation_algorithm = new TreeSubTreeMutation();
			break;
		case "FUNCION":
			this.algorithmData.mutation_algorithm = new FunctionMutation();
			break;
		}
	}

	/**
	 * Sets the number of dimensions for the function 4a and 4b
	 * 
	 * @param dimensions Number of dimensions
	 */
	public void setDimensions(int dimensions) {
		log("Dimensions: " + dimensions);
		algorithmData.dimensions = dimensions;
	}

	/**
	 * Sets cross chance
	 * 
	 * @param cross_chance Cross chance in percentage (0 - 100%)
	 */
	public void setCrossChance(double cross_chance) {
		log("Cross chance: " + cross_chance);
		this.algorithmData.cross_chance = cross_chance;
	}

	public void setCrossRangeChance(double min, double max) {
		log("Cross chance min-max: :" + min + ", " + max);
		this.cross_range.min_value = min;
		this.cross_range.max_value = max;
	}

	/**
	 * Sets mutation chance
	 * 
	 * @param mutation_chance Mutation chance in percentage (0 - 100%)
	 */
	public void setMutationChance(double mutation_chance) {
		log("Mutation chance: " + mutation_chance);
		this.algorithmData.mutation_chance = mutation_chance;
	}

	public void setMutationRangeChance(double min, double max) {
		log("Mutation chance min-max: :" + min + ", " + max);
		this.mutation_range.min_value = min;
		this.mutation_range.max_value = max;
	}

	public void setInicializationType(String inicializationType) {
		log("Inicialization type: " + inicializationType);
		this.algorithmData.inicializationType = inicializationType;
	}

	public void stop() {
		tryStopThread(controllerRunThread);
		view.setProgressBarPercentage(0);
		view.enableDisableStopButton(false);
	}

}
