package GeneticAlgorithm;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Rioni
 *
 * @param <T> Gene type
 * @param <U> Fenotype type
 */
public abstract class Chromosome<T,U> {
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
	
	//OVERRIDE METHODS------------------------------------------------
	public abstract void calculateFenotypes();
	public abstract double evaluate();
	protected abstract void initGenes();
	protected abstract Chromosome getNewInstance();
	
	//UTILITY--------------------------------------------------------
	// Gets Lenght for a given gen
	public int calculateGenSize(double tolerance, double min, double max)
	{
		return (int) (Math.log10(((max - min) / tolerance) + 1) / Math.log10(2));
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
	
	//GET & SET--------------------------------------------------
	public int getLenght() 	{ return this.num_of_genes;	}

	// Returns gene at pos if it exists and null otherwise.
	public Gene<T> getGene(int pos)
	{
		if(pos >= genes.length)
			return null;
		
		return genes[pos];
	}

	// Sets gene at pos if it exists and returns true. Otherwise returns false.
	public boolean setGene(int pos, Gene<T> gene)
	{
		if(pos >= genes.length)
			return false;
		
		genes[pos] = gene;
		return true;
	}

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
