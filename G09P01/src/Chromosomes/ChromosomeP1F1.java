package Chromosomes;

import java.util.Random;

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
	protected Chromosome getNewInstance() {
		return new ChromosomeP1F1(this.chromosomeLenght, this.tolerance);
	}
	
	@Override
	protected void initGenes() {
		final double min_x1 = -3.000;
		final double min_x2 = 4.100;
		
		final double max_x1 = 12.100;
		final double max_x2 = 5.800;
		
		final int tam_genes_x1 = this.calculateGenSize(this.tolerance, min_x1, max_x1);
		final int tam_genes_x2 = this.calculateGenSize(this.tolerance, min_x2, max_x2);
		
		this.genes[0] = new Gene<Boolean>(tam_genes_x1);
		this.genes[1] = new Gene<Boolean>(tam_genes_x2);

		Random rand = new Random();
		
		// Random init each gene
		for (int i = 0; i < this.chromosomeLenght; i++) {
			for (int j = 0; j < this.genes[i].getLenght(); j++) {
				this.genes[i].setAllele(j, rand.nextBoolean());
			}
		}
	}
}
