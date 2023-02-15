package Chromosomes;

import GeneticAlgorithm.Chromosome;
import GeneticAlgorithm.Gene;

public class ChromosomeP1F1 extends Chromosome<Boolean, Double> {

	public ChromosomeP1F1(int chromosomeLenght, double tolerance) {
		super(chromosomeLenght, tolerance);
	}

	@Override
    public void calculateFenotypes(){
		//ONLY CALCULATES ONCE
		if(fenotypes.isEmpty()) {
			for (Gene<Boolean> gene : genes) {
				double fenotype = bin_Dec(gene); 
				fenotypes.add(fenotype);
			}			
		}		
	}
	
	@Override 
	public void calculateFitness() {
		if(fenotypes.size() == 2) { // X1 && X2
			final double x1 = fenotypes.get(0);
			final double x2 = fenotypes.get(1);
			
			fitness = (21.5 + x1 * Math.sin(4 * Math.PI * x1)) + x2 * Math.cos(20 * Math.PI * x2);
		}
		else {
			fitness = Double.MIN_VALUE; //default value,if fails, wortst value
			System.out.println("Ejer 1: Wrong number of fitness params.");
		}		
	}

	@Override
	protected void initGenes() {
		// TODO Auto-generated method stub
		
	}
}
