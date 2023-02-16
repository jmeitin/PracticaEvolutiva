package GeneticAlgorithm;

import java.util.LinkedList;
import java.util.List;

public class Gene<T> {
	protected T[] alleles;
	protected int geneLenght;
	
	public Gene(int geneLenght) {
		this.alleles = (T[]) new Object[geneLenght];
		this.geneLenght = geneLenght;
	}
	
	public int getLenght() 
	{
		return this.geneLenght;
	}
	
	// Returns allele at pos if it exists and null otherwise.
	public T getAllele(int pos)
	{
		if(pos >= alleles.length)
			return null;
		
		return alleles[pos];
	}
	
	public final T[] getAlleles()
	{
		return this.alleles;
	}

	// Sets allele at pos if it exists and returns true. Otherwise returns false.
	public boolean setAllele(int pos, T allele)
	{
		if(pos >= alleles.length)
			return false;
		
		alleles[pos] = allele;
		return true;
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

	public Gene getCopy()
	{
		Gene<T> gene = new Gene<T>(this.geneLenght);
		
		for(int i = 0; i < alleles.length; i++)
			gene.setAllele(i, alleles[i]);
		
		return gene;
	}
}
