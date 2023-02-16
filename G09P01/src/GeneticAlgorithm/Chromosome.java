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
	
	protected int chromosomeLenght;
	protected double tolerance;
	protected double adaptation;
	protected double escalation;

	public Chromosome(int geneLenght, double tolerance) {
		this.chromosomeLenght = geneLenght;
		this.tolerance = tolerance;
		
		initGenes();
		calculateFenotypes();
	}
	
	//OVERRIDE METHODS------------------------------------------------
	public abstract void calculateFenotypes();
	public abstract double evaluate();
	protected abstract void initGenes();
	
	//UTILITY--------------------------------------------------------
	//Converts the chromosome to its decimal representation.
	public double binary_to_decimal(Gene<T> gen)
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
	
	public abstract Chromosome getCopy();
}
