package Evolutiva;

import javax.swing.JFrame;

import org.math.plot.*;

import Chromosomes.ChromosomeP1F1;
import GeneticAlgorithm.Chromosome;
import GeneticAlgorithm.ChromosomeFactory;
import GeneticAlgorithm.GeneticAlgorithm;
import View.MainView;

public class main {
	
	public static void main(String args[]) {
		//System.out.println("Practica 1 Ejecuto correctamente");
		
		// TEORIA PARA LA NOMENCLATURA:
		// n GENERACIONES, s DESCENDIENTES por iteracion
		// El Cromosoma (0010) tiene 4 GENES, con LOCUS/posiciones (0, 1, 2, 3) & ALELOS/valores (0, 1, 0, 0)
		// El FENOTIPO de 0100 es 4
		
//		double[] generaciones = { 1, 2, 3, 4, 5, 6, 7, 8, 9 ,10 };
//		double[] fitness = { 12, 25, 32, 45, 65, 67 , 70, 72, 73, 76};
//		// create your PlotPanel (you can use it as a JPanel)
//		Plot2DPanel plot = new Plot2DPanel();
//		// define the legend position
//		plot.addLegend("SOUTH");
//		// add a line plot to the PlotPanel
//		plot.addLinePlot("EVOLUCIÓN", generaciones, fitness);
//		// put the PlotPanel in a JFrame like a JPanel
//		JFrame frame = new JFrame("a plot panel");
//		frame.setSize(600, 600);
//		frame.setContentPane(plot);
//		frame.setVisible(true);
		
		run();
		ChromosomeFactory<Boolean, Double> chromosomeFactory = (double tolerance, int dimensions) -> {
			return new ChromosomeP1F1(10, tolerance);
		};
		
		GeneticAlgorithm<Boolean, Double> ag = new GeneticAlgorithm<Boolean, Double>(100, 100, 0.2, 0.05, 1, true, 1, chromosomeFactory);
		ag.run();
		
		MainView view = new MainView();
		view.setVisible(true);
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
