package Chromosomes;

import java.util.Random;

public class ChromosomeP2 extends Chromosome<Integer, Integer> {
	protected int[] genes;
	
	private final int MADRID_INDEX = 25;
	private static final int CITIES_COUNT = CitiesData.getNumCiudades();
	private static final int GEN_SIZE = CITIES_COUNT - 1;
	
	public ChromosomeP2(double tolerance) {
		super(GEN_SIZE); // 27 cities to travel, Madrid is always start and end so it's excluded
		genes = new int[this.num_of_genes];
		
		initGenes();
		calculateFenotypes();
	}

	@Override
	protected void initGenes() {
		
		// Init array from 0 to 26 without 25 (Madrid)
		int j = 0;
		for(int i = 0; i < CITIES_COUNT; i++)
		{
			if(i == MADRID_INDEX)
				continue;
			
			genes[j] = i;
			j++;
		}

		// Shuffle the array (Fisher-Yates method)
		Random random = new Random();
		for (int i = genes.length - 1; i > 0; i--) { 
		    j = random.nextInt(i + 1); // random number between 0 and i (inclusive)
		    int temp = genes[i];
		    genes[i] = genes[j];
		    genes[j] = temp;
		}
	}
	
	@Override
	public void calculateFenotypes() {
		fenotypes = new Integer[num_of_genes];
		
		// Fenotype and genotype coincide so not necessary for now
		for(int i = 0; i < genes.length; i++)
		{
			this.fenotypes[i] = genes[i];
		}
	}
	
	public final int[] getGenesRef()
	{
		return this.genes;
	}
	
	public int[] getGenesCopy()
	{
		int[] genes_copy = new int[genes.length];
		for(int i = 0; i < genes.length; i++)
		{
			genes_copy[i] = genes[i];
		}
		
		return genes_copy;
	}
	
	public boolean setGenes(int[] g) {
		genes = g.clone();
		if (g.length == genes.length) {
			genes = g.clone();
			return true;
		}
		return false;
	}
	
	public boolean setGene(int pos, int value) {
		if (0 <= pos && pos < genes.length) {
			genes[pos] = value;
			return true;
		}
		else return false;
	}
	
	public int getGeneInt(int pos) {
		if (0 <= pos && pos < genes.length)
			return genes[pos];
		else return -1;
	}

	@Override
	public double evaluate() {
		// Distance from Madrid to first city
		brute_fitness = CitiesData.getDistance(MADRID_INDEX, genes[0]);
		
		// Distance from each city to each other
		final int num_Cities = genes.length;
		for(int i = 0; i < num_Cities - 1; i++)
		{
			brute_fitness += CitiesData.getDistance(genes[i], genes[i+1]);
		}
		
		// Distance from last city to Madrid
		brute_fitness += CitiesData.getDistance(genes[num_Cities -1], MADRID_INDEX);
		
		return brute_fitness;
	}

	@Override
	protected Chromosome getNewInstance() {
		return new ChromosomeP2(this.tolerance);
	}

	@Override
	protected boolean mutateGene(int pos, Random rand, double mutation_chance) {
		// TODO Auto-generated method stub
		return false;
	}

	
	// Returns a copy of this Chromosome
	@Override
	public Chromosome getCopy()
	{
		ChromosomeP2 chromosome = (ChromosomeP2) getNewInstance();
		chromosome.setFitness(this.fitness);	
		chromosome.setBruteFitness(this.brute_fitness);
		chromosome.setScore(this.score);
		chromosome.setAccumulatedScore(this.scoreAccumulated);
		chromosome.fenotypes = this.fenotypes;
		chromosome.num_of_genes = this.num_of_genes;
		chromosome.genes = this.getGenesCopy();
		
		return chromosome;
	};
}
