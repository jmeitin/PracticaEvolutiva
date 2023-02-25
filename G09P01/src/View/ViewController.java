package View;

import Chromosomes.ChromosomeP1F1;
import CrossAlgorithms.OnePointCross;
import GeneticAlgorithm.ChromosomeFactory;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.GeneticAlgorithmData;
import SelectionAlgorithms.RouletteSelection;

public class ViewController implements Runnable {

	private class ModelRunner implements Runnable {
		@Override
		public void run() {
			geneticAlgorithm = new GeneticAlgorithm<Boolean, Double>(algorithmData);
			geneticAlgorithm.run();
		}
	}

	private final MainView view;
	private GeneticAlgorithm geneticAlgorithm;
	private ChromosomeFactory chromosomeFactory;
	private GeneticAlgorithmData algorithmData = new GeneticAlgorithmData();
	private Thread modelThread;
	private Thread controllerRunThread;

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
		algorithmData.maximize = true;
		algorithmData.dimensions = 1;
		algorithmData.chromosomeFactory = chromosomeFactory;
		algorithmData.selectionAlgorithm = new RouletteSelection();
		algorithmData.crossAlgorithm = new OnePointCross();
	}

	private void runAux() {
		if(tryStopThread(modelThread)) // Cancel thread if already running
			geneticAlgorithm.stop();

		modelThread = new Thread(new ModelRunner());
		modelThread.start();

		while (modelThread.isAlive()) {
			try {
				Thread.sleep(1000); // Wait 1 second
			} catch (InterruptedException e) {
				geneticAlgorithm.stop();
			}

			// UpdateView gatheting model data
			updateView();
		}
		
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
			System.out.println("Stopped");
			return true;
		}
		
		return false;
	}

	private void updateView() {

	}

	private void wait(int miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
