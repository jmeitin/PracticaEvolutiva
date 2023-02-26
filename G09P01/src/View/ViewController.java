package View;

import java.awt.Color;

import Chromosomes.ChromosomeP1F1;
import CrossAlgorithms.OnePointCross;
import GeneticAlgorithm.Chromosome;
import GeneticAlgorithm.ChromosomeFactory;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.GeneticAlgorithmData;
import MutationAlgorithm.MutationAlgorithm;
import SelectionAlgorithms.RouletteSelection;

public class ViewController implements Runnable {

	private class ModelRunner implements Runnable {
		@Override
		public void run() {
			geneticAlgorithm = new GeneticAlgorithm<Boolean, Double>(getAlgorithmData());
			geneticAlgorithm.run();
		}
	}

	private final MainView view;
	private GeneticAlgorithm geneticAlgorithm;
	private ChromosomeFactory chromosomeFactory;
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

	public ViewController(final MainView view) {
		this.view = view;
		chromosomeFactory = (double tolerance, int dimensions) -> {
			return new ChromosomeP1F1(2, tolerance);
		};

		algorithmData.poblation_size = 100;
		algorithmData.max_gen_num = 100;
		algorithmData.cross_chance = 0.2;
		algorithmData.mutation_chance = 0.05;
		algorithmData.tolerance = 0.025;
		algorithmData.elitism_percentage = 0.0;
		algorithmData.maximize = true;
		algorithmData.dimensions = 1;
		algorithmData.chromosomeFactory = chromosomeFactory;
		algorithmData.selectionAlgorithm = new RouletteSelection();
		algorithmData.crossAlgorithm = new OnePointCross();
		algorithmData.mutationAlgorithm = new MutationAlgorithm();
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
	
	private void updateSolution()
	{
		String solutionText = "";
		Chromosome chromosome = this.geneticAlgorithm.getBest_chromosome();
		for (Object fenotype : chromosome.getFenotypes())
		{
			solutionText += "Variable X1 = " + fenotype.toString() + ", ";
		}
		
		solutionText += "Valor de la función: " + chromosome.evaluate();
		solutionText += " Fitness del cromosoma " + chromosome.getFitness();
		
		this.view.setSolutionText(solutionText);
	}
	
	private void wait(int miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * @return the algorithmData
	 */
	public GeneticAlgorithmData getAlgorithmData() {
		return algorithmData;
	}
	
	// View Interaction
	public void setPoblationSize(int poblation_size)
	{
		this.algorithmData.poblation_size = poblation_size;
	}
	
	public void setGenSize(int generation_size)
	{
		this.algorithmData.max_gen_num = generation_size;
	}
	
	public void setElitism(double elitism_percentage)
	{
		this.algorithmData.elitism_percentage = elitism_percentage;
	}
	
	public void setTolerance(double tolerance)
	{
		this.algorithmData.tolerance = tolerance;
	}
	
	public void setFunction(String function)
	{
		// This parameter depends on the function so it should be set here.
		//algorithmData.maximize = true;
	}
	
	public void setSelectionType(String selection)
	{
		System.out.println(selection);
		switch(selection)
		{
			case "RULETA":
				this.algorithmData.selectionAlgorithm = new RouletteSelection();
				break;
			case "ESTOCÁSTICO":
				System.out.println("Funcionan las palabras con tildes (los strings seran UTF-8)");
				break;
		}
	}
	
	public void setCrossType(String cross)
	{
		
	}
	
	public void setMutationType(String mutationType)
	{
		//solo hay 1 tipo de mutacion (creo)
	}
	
	public void setCrossChance(double cross_chance)
	{
		this.algorithmData.cross_chance = cross_chance;
	}
	
	public void setMutationChance(double mutation_chance)
	{
		this.algorithmData.mutation_chance = mutation_chance;
	}
	
}
