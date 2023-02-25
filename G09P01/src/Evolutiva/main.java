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
		MainView view = new MainView();

	
		
	
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
