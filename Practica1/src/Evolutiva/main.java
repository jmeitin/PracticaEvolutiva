package Evolutiva;

import javax.swing.JFrame;

import org.math.plot.*;

public class main {
	
	public static void main(String args[]) {
		//System.out.println("Practica 1");
		
		double[] generaciones = { 1, 2, 3, 4, 5, 6, 7, 8, 9 ,10 };
		double[] fitness = { 12, 25, 32, 45, 65, 67 , 70, 72, 73, 76};
		// create your PlotPanel (you can use it as a JPanel)
		Plot2DPanel plot = new Plot2DPanel();
		// define the legend position
		plot.addLegend("SOUTH");
		// add a line plot to the PlotPanel
		plot.addLinePlot("EVOLUCIÓN", generaciones, fitness);
		// put the PlotPanel in a JFrame like a JPanel
		JFrame frame = new JFrame("a plot panel");
		frame.setSize(600, 600);
		frame.setContentPane(plot);
		frame.setVisible(true);
	}
}
