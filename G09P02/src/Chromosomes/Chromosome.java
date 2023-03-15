package Chromosomes;

import java.util.Random;

/**
 * 
 * @author Rioni
 *
 * @param <T> Gene type
 * @param <U> Fenotype type
 */
public abstract class Chromosome<T,U> implements Comparable<Chromosome> {
	protected Gene<T>[] genes;
	protected U[] fenotypes;
	
	protected double fitness;
	protected double brute_fitness;
	protected double score;
	protected double scoreAccumulated;
	
	protected int num_of_genes;
	protected double tolerance;
	protected double adaptation;
	protected double escalation;

	public Chromosome(int geneLenght, double tolerance) {
		this.num_of_genes = geneLenght;
		this.tolerance = tolerance;
		genes = (Gene<T>[]) new Gene[geneLenght];
		
		initGenes();
		calculateFenotypes();
	}
	
	// OVERRIDE METHODS------------------------------------------------
	public abstract void calculateFenotypes();
	public abstract double evaluate();
	protected abstract void initGenes();
	protected abstract Chromosome getNewInstance();
	protected abstract boolean mutateGene(int pos, Random rand, double mutation_chance);
	
	// UTILITY--------------------------------------------------------
	// Gets Lenght for a given gen
	public int calculateGenSize(double tolerance, double min, double max)
	{
		return (int) (Math.log10(((max - min) / tolerance) + 1) / Math.log10(2));
	}
	
	@Override
	public int compareTo(Chromosome other)
	{ 
		// if other < this ==> -1
		// if == ==> 0
		// if other > this ==> 1
		return Double.compare(other.fitness,  this.fitness);
	}
	
	//Converts the chromosome to its decimal representation.
	public double binaryToDecimal(Gene<T> gen)
	{
		double dec = 0;
		int i = 0;
		for(T allele : gen.getAlleles())
		{
			if(allele.toString().equals("true"))
				dec += Math.pow(2, i);
			i++;
		}
		return dec;
	}
	
	// Returns a copy of this Chromosome
	public Chromosome getCopy()
	{
		Chromosome chromosome = getNewInstance();
		chromosome.setFitness(this.fitness);	
		chromosome.setBruteFitness(this.brute_fitness);
		chromosome.setScore(this.score);
		chromosome.setAccumulatedScore(this.scoreAccumulated);
		chromosome.fenotypes = this.fenotypes;
		chromosome.num_of_genes = this.num_of_genes;

		for(int i = 0; i < genes.length; i++)
			chromosome.setGene(i, genes[i].getCopy());
		
		return chromosome;
	};
	
	// Returns num_of_genes
	public int getNumOfGenes() 	{ return this.num_of_genes;	}
	
	public int getNumAllelesInGene(){
		if (this.num_of_genes == 0)
			return 0;
		return genes[0].getLenght();
	}

	// Returns gene at pos if it exists and null otherwise.
	public Gene<T> getGene(int pos)
	{
		if(pos >= genes.length)
			return null;
		
		return genes[pos];
	}
	
	// Swaps alleles of 2 genes if random < cross_chance. Starts swapping in allele_pos
	public boolean swapAllelesInGene(Chromosome<T,U> other_chromosome, int pos, int allele_pos, Random rand, double cross_chance)
	{
		int num_alleles = genes[0].getLenght();
		if(pos * num_alleles + allele_pos >= genes.length * num_alleles && allele_pos < num_alleles)
			return false;
		
		// Swap alleles in gene [pos]
		for (int a = allele_pos; a < num_alleles; a++) {
			if (rand.nextDouble() < cross_chance) { // [0.0, 1.0]
				T allele = genes[pos].getAllele(a);
				genes[pos].setAllele (a, other_chromosome.getGene(pos).getAllele(a));
				other_chromosome.getGene(pos).setAllele(a, allele);
			}
		}
		
		return true;
	}

	/**
	 * Exchange genes with other chromosome
	 * @param pos Position of the gen to chance
	 * @param other_chromosome The other chromosome to swap genes with
	 * @return Wheter the swap succeeded
	 */
	public boolean swapGene(int pos, Chromosome<T,U> other_chromosome)
	{
		if(pos >= genes.length || pos <= 0)
			return false;
		
		Gene<T> g = genes[pos];
		genes[pos] = other_chromosome.getGene(pos);
		other_chromosome.setGene(pos, g);
		
		return true;
	}
	
	//Displace genes to the right in segment[init, fin]
	public boolean displaceGenes(int init, int fin) {
		if(0 <= init && init < fin && fin < num_of_genes) {
			int i = fin;
			Gene<T> last = genes[fin];
			while (i > init) {				
				genes[i] = genes[i-1];
				i--;				
			}
			genes[init] = last;
			return true;
		}
		return false;
		
	}
	
	/***
	 * Mutates gene bitwise (if boolean chromsoome) and random (if real)
	 * @param rand Random object
	 * @param mutation_chance Probability of the bit to mutate (0-100%)
	 */
	public void mutate(Random rand, double mutation_chance) {
		boolean changes = false;
		
		for (int i=0; i < num_of_genes; i++)
			changes = mutateGene(i, rand, mutation_chance);
		
		if (changes)
			calculateFenotypes(); // Update fenotype
	}
	
	/**
	 * Sets a gene in pos
	 * @param pos
	 * @param gene
	 * @return
	 */
	public boolean setGene(int pos, Gene<T> gene)
	{
		if(pos >= genes.length)
			return false;
		
		genes[pos] = gene;
		return true;
	}
	
	// Getters and settters, we aren't adding doc for that

	public double getFitness() { return fitness;	}

	public void setFitness(double fitness) { this.fitness = fitness;}
	
	public double getBruteFitness() { return brute_fitness; }

	public void setBruteFitness(double brute_fitness) { this.brute_fitness = brute_fitness; }

	public U[] getFenotypes(){ return fenotypes; }

	public double getScore() { return score; }

	public void setScore(double score) { this.score = score; }

	public double getAccumulatedScore() { return scoreAccumulated; }

	public void setAccumulatedScore(double scoreAccumulated) { this.scoreAccumulated = scoreAccumulated; }
}
