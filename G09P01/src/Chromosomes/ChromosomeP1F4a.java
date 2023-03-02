package Chromosomes;

import java.util.Random;

import GeneticAlgorithm.Chromosome;
import GeneticAlgorithm.Gene;

public class ChromosomeP1F4a extends Chromosome<Boolean, Double> {
	//que presenta los siguientes mínimos en función de d:
//	        d= 2 f(x*) = - 1.8013 en x* = (2.20, 1.57)
//			d = 5 f(x*) = - 4.6876
//			d = 10 f(x*) = - 9.6601
	final double min_x = 0;
	final double max_x = Math.PI;
	final double m = 10;
	
	public ChromosomeP1F4a(int chromosomeLenght, double tolerance) {
		super(chromosomeLenght, tolerance);
		//Chromosome calls initGenes & calculateFenotypes
	}
	
	@Override
	public int compareTo(Chromosome other)
	{
		return Double.compare(this.fitness, other.getFitness());
	}
	
	@Override
	protected void initGenes() {
		final int tam_genes_x = this.calculateGenSize(this.tolerance, min_x, max_x);
		
		Random rand = new Random();

		// Random init each gene
		//num_of_genes = d = 2
		for (int i = 0; i < this.num_of_genes; i++) {
			this.genes[i] = new Gene<Boolean>(tam_genes_x);
			
			for (int j = 0; j < this.genes[i].getLenght(); j++) {
				this.genes[i].setAllele(j, rand.nextBoolean());
			}
		}
	}

	@Override
	public void calculateFenotypes() {
		fenotypes = new Double[num_of_genes];
		
		for (int i = 0; i < num_of_genes; i++) {
			fenotypes[i] = min_x + (max_x - min_x) * (binaryToDecimal(genes[i]) / (Math.pow(2, genes[i].getLenght()) - 1));
		}
		//fenotypes[0] = min_x1 + (max_x1 - min_x1) * (binaryToDecimal(genes[0]) / (Math.pow(2, genes[0].getLenght()) - 1));
		
	}

	@Override
	public double evaluate() {
		if (fenotypes.length == num_of_genes) { // X1 && X2
//			final double x1 = fenotypes[0];
//			final double x2 = fenotypes[1];
			
			double sumatorio = 0;
			for (int i = 0; i < num_of_genes; i++) {
				double x_i = fenotypes[i];
				double angle = (i * Math.pow(x_i, 2)) / Math.PI;
				sumatorio += Math.sin(x_i) * Math.pow(angle, 2*m);	        
			}
			
			brute_fitness = - sumatorio;
		} else {
			brute_fitness = Double.MIN_VALUE; // default value,if fails, wortst value
			System.out.println("Ejer 1: Wrong number of fitness params.");
		}

		return brute_fitness;
	}
	
	@Override
	public boolean mutateGene(int pos, Random rand, double mutation_chance) {
		boolean cambios = false;
		
		if(0 <= pos && pos < num_of_genes){
			//Random rand = new Random();
			for (int j = 0; j < this.genes[pos].getLenght(); j++) {
				if (rand.nextDouble() < mutation_chance) {
					this.genes[pos].setAllele(j, rand.nextBoolean());
					cambios = true;
				}				
			}
		}	
		return cambios;
	}

	@Override
	protected Chromosome getNewInstance() {
		return new ChromosomeP1F4a(this.num_of_genes, this.tolerance);
	}

	
}
