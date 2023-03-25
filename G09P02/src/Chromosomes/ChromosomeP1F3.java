package Chromosomes;

import java.util.Random;

public class ChromosomeP1F3 extends Chromosome<Boolean, Double> {
	//n mínimo global de de -39.16599d en x1 = -2.903534 y x2 = -2.9035340.
	final double min_x = -5;
	final double max_x = 5;
	
	public ChromosomeP1F3(int chromosomeLenght, double tolerance) {
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
				sumatorio += Math.pow(x_i, 4) - 16 * Math.pow(x_i, 2) + 5 * x_i;		        
			}
			
			brute_fitness = (sumatorio) * 0.5;
		} else {
			brute_fitness = Double.MAX_VALUE; // default value,if fails, wortst value
			System.out.println("Ejer 3: Wrong number of fitness params.");
		}

		return brute_fitness;
	}
	
	@Override
	public boolean mutateGene(int pos, Random rand, double mutation_chance) {
		boolean cambios = false;
		
		if(0 <= pos && pos < num_of_genes){
			//Random rand = new Random();
			for (int j = 0; j < this.genes[pos].getLenght(); j++) {
				if (rand.nextDouble()  * 100 < mutation_chance) {
					this.genes[pos].setAllele(j, rand.nextBoolean());
					cambios = true;
				}				
			}
		}	
		return cambios;
	}

	@Override
	protected Chromosome getNewInstance() {
		return new ChromosomeP1F3(this.num_of_genes, this.tolerance);
	}

	
}
