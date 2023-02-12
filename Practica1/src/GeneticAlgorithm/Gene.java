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
	
	public final List<T> getAlleles()
	{
		return this.alleles;
	}

	// Sets allele at pos if it exists and returns true. Otherwise returns false.
	public boolean setAllele(int pos, T allele)
	{
		if(pos >= alleles.size())
			return false;
		
		alleles.set(pos, allele);
		return true;
	}
	
	// Adds an allele to the gene.
	public void addAllele(T allele)
	{
		alleles.add(allele);
	}

	// Returns the gene as a string
	public String toString()
	{
		String gene = "";
		
		for(T allele : alleles)
		{
			gene += allele.toString();
		}
		
		return gene;
	}
}
