package GeneticAlgorithm;

import java.util.List;

/**
 * 
 * @author Rioni
 *
 * @param <T> Gene type
 * @param <U> Fenotype type
 */
public abstract class Chromosome<T,U> {
	protected List<Gene<T>> genes;
	protected List<U> fenotypes;
	
	protected double fitness;
	protected double brute_fitness;
	protected double score;
	protected double scoreAccumulated;
	
	protected int chromosomeLenght;
	protected double tolerance;
	protected double adaptation;
	protected double escalation;

	public Chromosome(int chromosomeLenght, double tolerance) {
		this.chromosomeLenght = chromosomeLenght;
		this.tolerance = tolerance;
	}
	//OVERRIDE METHODS------------------------------------------------
	public abstract void calculateFenotypes();
	public abstract void calculateFitness();
	
	//UTILITY--------------------------------------------------------
	//Converts the chromosome to its decimal representation.
	public double bin_Dec(Gene<T> gen)
	{
		double dec = 0;
		int i = 0;
		for(T allele : gen.getAlleles())
		{
			if(allele.toString().equals("1"))
				dec += Math.pow(2, i);
			i++;
		}
		return dec;
	}
	
	//GET & SET--------------------------------------------------
	public int getLenght() 	{ return this.chromosomeLenght;	}

	// Returns gene at pos if it exists and null otherwise.
	public Gene<T> getGene(int pos)
	{
		if(pos >= genes.size())
			return null;
		
		return genes.get(pos);
	}

	// Sets gene at pos if it exists and returns true. Otherwise returns false.
	public boolean setGene(int pos, Gene<T> gene)
	{
		if(pos >= genes.size())
			return false;
		
		genes.set(pos, gene);
		return true;
	}

	// Adds a gene to the chromosome.
	public void addGene(Gene<T> gene) { genes.add(gene);	}	

	public double getFitness() { return fitness;	}

	public void setFitness(double fitness) { this.fitness = fitness;}
	
	public double getBruteFitness() { return brute_fitness; }

	public void setBruteFitness(double brute_fitness) { this.brute_fitness = brute_fitness; }

	public List<U> getFenotypes(){ return fenotypes; }
}
