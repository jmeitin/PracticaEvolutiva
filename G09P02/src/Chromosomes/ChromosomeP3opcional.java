package Chromosomes;

import java.util.Random;

public class ChromosomeP3opcional extends Chromosome<Integer, Integer> {
	final private String[] start = {"<expr> <op> <expr>", "<expr>"};
	final private String[] expr = {"<term> <op> <term>", "(<term> <op> <term>)", "<digit> <op> <expr>", "(<digit> <op> <expr>)"};
	final private String[] op = {"add", "sub", "mul"};
	final private String[] term = {"x"};
	final private String[] digit = {"-2", "-1", "0", "1", "2"};
	private int max_value = 256; //[0, 255]
	
	private String fenotype = "";
	
	//CHROMOSOME LENGTH ES CONFIGURABLE DESDE EL EDITOR	
	public ChromosomeP3opcional(int chromosomeLenght, double tolerance) {
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
		//EACH GENE IS FORMED BY 1 REAL NUMBER | ALLELE ONLY
		final int tam_genes_x = 1; //this.calculateGenSize(this.tolerance, min_x, max_x);
		
		Random rand = new Random();

		// Random init each gene
		//num_of_genes = d = 6
		for (int i = 0; i < this.num_of_genes; i++) {
			this.genes[i] = new Gene<Integer>(tam_genes_x);
			
			for (int j = 0; j < this.genes[i].getLenght(); j++) {
				//Each genes has only 1 allele
				this.genes[i].setAllele(j, rand.nextInt(max_value)); // [0, 255]
			}
		}
	}

	@Override
	public void calculateFenotypes() {
		int index = genes[0].getAllele(0) % start.length;
		fenotype = start[index];
		
		for (int i = 1; i < num_of_genes; i++) {
			
		}
//		fenotypes = new Integer[num_of_genes];
//		
//		for (int i = 0; i < num_of_genes; i++) {
//			// EACH GENE CONTAINS ONLY 1 ALLELE. FENOTYPE = VALUE IN ALLELE
//			fenotypes[i] = genes[i].getAllele(0);
//		}
	}

	@Override
	public double evaluate() {
//		if (fenotypes.length == num_of_genes) { 
//			brute_fitness = 0;
//			
//			// RELLENAR-----------------------------------
//			
//		} else {
//			brute_fitness = Double.MAX_VALUE; // default value,if fails, wortst value
//			System.out.println("Ejer 4: Wrong number of fitness params.");
//		}
		brute_fitness = 0;
		return brute_fitness;
	}
	
	@Override
	public boolean mutateGene(int pos, Random rand, double mutation_chance) {
		boolean cambios = false;
		
		if(0 <= pos && pos < num_of_genes){
			for (int j = 0; j < this.genes[pos].getLenght(); j++) {
				if (rand.nextDouble() * 100 < mutation_chance) {
					this.genes[pos].setAllele(j, rand.nextInt(max_value)); // [0, 255]
					cambios = true;
				}				
			}
		}	
		return cambios;
	}

	@Override
	protected Chromosome getNewInstance() {
		return new ChromosomeP3opcional(this.num_of_genes, this.tolerance);
	}

	
}
