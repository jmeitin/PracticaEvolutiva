package GeneticAlgorithm;

import java.util.LinkedList;
import java.util.List;

public class Gene<T> {
	protected List<T> alleles;
	protected int geneLenght;
	
	public Gene(int geneLenght) {
		this.alleles = new LinkedList<T>();
		this.geneLenght = geneLenght;
	}
	
	public int getLenght() 
	{
		return this.geneLenght;
	}
	
	
	// Returns allele at pos if it exists and null otherwise.
	public T getAllele(int pos)
	{
		if(pos >= alleles.size())
			return null;
		
		return alleles.get(pos);
	}
	
}
