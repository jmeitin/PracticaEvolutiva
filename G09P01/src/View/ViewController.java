package View;

import java.awt.Color;
import java.text.DecimalFormat;

import Chromosomes.ChromosomeP1F1;
import Chromosomes.ChromosomeP1F2;
import Chromosomes.ChromosomeP1F3;
import Chromosomes.ChromosomeP1F4a;
import Chromosomes.ChromosomeP1F4b;
import CrossAlgorithms.OnePointCross;
import CrossAlgorithms.UniformCross;
import CrossAlgorithms.ArithmeticCross;
import GeneticAlgorithm.Chromosome;
import GeneticAlgorithm.ChromosomeFactory;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.GeneticAlgorithmData;
import MutationAlgorithm.BasicGenMutation;
import MutationAlgorithm.MutationAlgorithm;
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

	private final boolean debugMode = true;
	private final MainView view;
	private GeneticAlgorithm geneticAlgorithm;
	private GeneticAlgorithmData algorithmData = new GeneticAlgorithmData();
	private Thread modelThread;
	private Thread controllerRunThread;

	// Colors
	private final Color lightBlue = new Color(34, 235, 249);
	private final Color lightGreen = new Color(67, 249, 34);
	private final Color lightRed = new Color(249, 34, 34);
	private final Color darkBlue = new Color(25, 111, 160);
	private final Color darkGreen = new Color(39, 160, 25);
	private final Color darkRed = new Color(160, 36, 25);

	private void log(String str) {
		if (debugMode)
			System.out.println(str);
	}

	public ViewController(final MainView view) {
		this.view = view;
	}

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

	public void run() {
		tryStopThread(controllerRunThread);

		controllerRunThread = new Thread(new Runnable() {
			public void run() {
				runAux();
			}
		});
		controllerRunThread.start();
	}

	private boolean tryStopThread(Thread thread) {
		if (thread != null && thread.isAlive()) {
			thread.interrupt();
			System.out.println("Thread Stopped");
			return true;
		}

		return false;
	}

	public void updateGraphsView() {
		if (geneticAlgorithm != null)
			view.updateGraph(this.geneticAlgorithm.getAverageFitnesses(),
					this.geneticAlgorithm.getBestAbsoluteFitnesses(), this.geneticAlgorithm.getBestFitnesses());
	}

	private String formatNumber(double number, int num_decimals)
	{
		String format = "0.";
        for (int i = 0; i < num_decimals; i++) {
            format += "0";
        }
        DecimalFormat df = new DecimalFormat(format);
        return df.format(number);
	}
	
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

	// View Interaction
	public void setPoblationSize(int poblation_size) {
		log("Poblation Size: " + poblation_size);
		this.algorithmData.poblation_size = poblation_size;
	}

	public void setGenSize(int generation_size) {
		log("Generation Number: " + generation_size);
		this.algorithmData.max_gen_num = generation_size;
	}

	public void setElitism(double elitism_percentage) {
		log("Elitism chance: " + elitism_percentage);
		this.algorithmData.elitism_percentage = elitism_percentage;
	}

	public void setTolerance(double tolerance) {
		log("Tolerance: " + tolerance);
		this.algorithmData.tolerance = tolerance;
	}

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

	public void setSelectionType(String selection) {
		log("Selection Type: " + selection);
		switch (selection) {
		case "RULETA":
			this.algorithmData.selectionAlgorithm = new RouletteSelection();
			break;
		case "T-DETERMINÍSTICO":
			this.algorithmData.selectionAlgorithm = new DeterministicTournamentSelection();
			break;
		case "T-PROBABILÍSTICO":
			this.algorithmData.selectionAlgorithm = new ProbabilisticTournamentSelection();
			break;
		case "ESTOCÁSTICO":
			this.algorithmData.selectionAlgorithm = new StochasticSelection();
			break;
		case "TRUNCAMIENTO":
			this.algorithmData.selectionAlgorithm = new TruncationSelection();
			break;
		case "RESTOS":
			this.algorithmData.selectionAlgorithm = new RemainderSelection();
			break;
		}
	}

	public void setCrossType(String cross) {
		log("Cross Type: " + cross);
		switch (cross) {
		case "MONOPUNTO":
			this.algorithmData.crossAlgorithm = new OnePointCross();
			break;
		case "UNIFORME":
			this.algorithmData.crossAlgorithm = new UniformCross();
			break;
		//ONLY CALLABLE DURING FUNCTION 4B--------------------------------------------------------------------
		case "ARITMÉTICO":
			this.algorithmData.crossAlgorithm = new ArithmeticCross();
			break;
		}
	}

	public void setMutationType(String mutationType) {
		log("Mutation Type: " + mutationType);
		switch (mutationType) {
		case "BÁSICA":
			this.algorithmData.mutationAlgorithm = new BasicGenMutation();
			break;
		}
	}

	public void setDimensions(int dimensions)
	{
		log("Dimensions: " + dimensions);
		algorithmData.dimensions = dimensions;
	}
	
	public void setCrossChance(double cross_chance) {
		log("Cross chance: " + cross_chance);
		this.algorithmData.cross_chance = cross_chance;
	}

	public void setMutationChance(double mutation_chance) {
		log("Mutation chance: " + mutation_chance);
		this.algorithmData.mutation_chance = mutation_chance;
	}

}
