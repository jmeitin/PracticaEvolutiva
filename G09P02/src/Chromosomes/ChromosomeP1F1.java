package Chromosomes;

import java.util.Random;

public class ChromosomeP1F1 extends Chromosome<Boolean, Double> {

	final double min_x1 = -3.000;
	final double min_x2 = 4.100;

	final double max_x1 = 12.100;
	final double max_x2 = 5.800;
	
	public ChromosomeP1F1(int chromosomeLenght, double tolerance) {
		super(chromosomeLenght, tolerance);
		//Chromosome calls initGenes & calculateFenotypes
	}
	
	@Override
	protected void initGenes() {
		final int tam_genes_x1 = this.calculateGenSize(this.tolerance, min_x1, max_x1);
		final int tam_genes_x2 = this.calculateGenSize(this.tolerance, min_x2, max_x2);

		this.genes[0] = new Gene<Boolean>(tam_genes_x1);
		this.genes[1] = new Gene<Boolean>(tam_genes_x2);

		Random rand = new Random();

		// Random init each gene
		for (int i = 0; i < this.num_of_genes; i++) {
			for (int j = 0; j < this.genes[i].getLenght(); j++) {
				this.genes[i].setAllele(j, rand.nextBoolean());
			}
		}
	}

	@Override
	public void calculateFenotypes() {
		fenotypes = new Double[num_of_genes];
		fenotypes[0] = min_x1 + (max_x1 - min_x1) * (binaryToDecimal(genes[0]) / (Math.pow(2, genes[0].getLenght()) - 1));
		fenotypes[1] = min_x2 + (max_x2 - min_x2) * (binaryToDecimal(genes[1]) / (Math.pow(2, genes[1].getLenght()) - 1));
	}

	@Override
	public double evaluate() {
		if (fenotypes.length == 2) { // X1 && X2
			final double x1 = fenotypes[0];
			final double x2 = fenotypes[1];

			brute_fitness = (21.5 + x1 * Math.sin(4 * Math.PI * x1)) + x2 * Math.cos(20 * Math.PI * x2);
		} else {
			brute_fitness = Double.MIN_VALUE; // default value,if fails, wortst value
			System.out.println("Ejer 1: Wrong number of fitness params.");
		}

		return brute_fitness;
	}
	
	@Override
	public boolean mutateGene(int pos, Random rand, double mutation_chance) {
		boolean cambios = false;
		System.out.println(mutation_chance);
		
		if(0 <= pos && pos < num_of_genes){
			//Random rand = new Random();
			for (int j = 0; j < this.genes[pos].getLenght(); j++) {
				if (rand.nextDouble() * 100 < mutation_chance) {
					
					this.genes[pos].setAllele(j, rand.nextBoolean());
					cambios = true;
				}				
			}
		}	
		return cambios;
	}

	@Override
	protected Chromosome getNewInstance() {
		return new ChromosomeP1F1(this.num_of_genes, this.tolerance);
	}

	
}
