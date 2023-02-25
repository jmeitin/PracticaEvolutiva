package Evolutiva;

import javax.swing.JFrame;

import org.math.plot.*;

import Chromosomes.ChromosomeP1F1;
import CrossAlgorithms.*;
import GeneticAlgorithm.Chromosome;
import GeneticAlgorithm.ChromosomeFactory;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.GeneticAlgorithmData;
import SelectionAlgorithms.*;
import View.MainView;

public class main {
	
	public static void main(String args[]) {
		// MainView view = new MainView();

		ChromosomeFactory<Boolean, Double> chromosomeFactory = (double tolerance, int dimensions) -> {
			return new ChromosomeP1F1(2, tolerance);
		};
		
		GeneticAlgorithmData algorithmData = new GeneticAlgorithmData();
		
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
	
		
		GeneticAlgorithm<Boolean, Double> ag = new GeneticAlgorithm<Boolean, Double>(algorithmData);
		ag.run();
	}
	
	private static void run()
	{
		
		int t=0;
		int MAX_NUM_GENERACIONES = 10;
		Boolean condicion = false;
		//igual no tiene que ser en paralelo? seria un array p [t]
		// Generar poblacion =  inicial(P(t));
		// Evaluar población(P(t)) en paralelo;
		
		// Para cada generacion
		while ((t < MAX_NUM_GENERACIONES) && !condicion) {
			t++;
			//Selecccion---------------------
//			Poblacion(t) = Selección(P(t-1));
//			//Cruce---------------------
//			Reproducción(P(t));;
//			//Mutacion---------------------
//			Mutacion(P(t));
//			//Evaluar---------------------
//			Evaluar población(P(t)) en paralelo;
		}
		
		//Devolver mejor
	}
	
	
//	public void mutacion(double probMutacion, Random r) {
//		boolean cambios = false;
//		for (int i=0; i < cromosoma.length; i++) {
//			if (r.nextDouble() < probMutacion) {
//				bits[i] = r.nextBoolean();
//				cambios = true;
//			}
//		}
//		if (cambios) {
//			refrescaFenotipo();
//		}
//	}
}
