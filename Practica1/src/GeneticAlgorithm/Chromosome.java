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
	protected double score;
	protected double scoreAccumulated;
	
	protected int chromosomeLenght;
	protected double tolerance;
	protected double adaptation;
	protected double escalation;
}
