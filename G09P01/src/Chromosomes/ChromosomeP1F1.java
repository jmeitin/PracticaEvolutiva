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
		if(fenotypes.length == 0) {
			fenotypes = new Double[this.chromosomeLenght];
			int i = 0;
			for (Gene<Boolean> gene : genes) {
				fenotypes[i] = binaryToDecimal(gene);
				i++;
			}			
		}		
	}
	
	@Override 
	public double evaluate() {
		if(fenotypes.length == 2) { // X1 && X2
			final double x1 = fenotypes[0];
			final double x2 = fenotypes[1];
			
			brute_fitness = (21.5 + x1 * Math.sin(4 * Math.PI * x1)) + x2 * Math.cos(20 * Math.PI * x2);
		}
		else {
			brute_fitness = Double.MIN_VALUE; //default value,if fails, wortst value
			System.out.println("Ejer 1: Wrong number of fitness params.");
		}
		
		return brute_fitness;
	}

	@Override
	protected void initGenes() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Chromosome getCopy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Chromosome getNewInstance() {
		return new ChromosomeP1F1(this.chromosomeLenght, this.tolerance);
	}
}
