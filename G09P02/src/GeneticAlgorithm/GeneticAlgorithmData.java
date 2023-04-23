package GeneticAlgorithm;

import Chromosomes.ChromosomeFactory;
import CrossAlgorithms.CrossAlgorithm;
import MutationAlgorithm.MutationAlgorithm;
import SelectionAlgorithms.SelectionAlgorithm;

public class GeneticAlgorithmData {
	public int poblation_size;
	public int max_gen_num;
	public double cross_chance;
	public double mutation_chance;
	public double tolerance;
	public double elitism_percentage;
	public boolean maximize;
	public int dimensions;
	public String inicializationType;
	public ChromosomeFactory chromosome_factory;
	public SelectionAlgorithm selection_algorithm;
	public CrossAlgorithm cross_algorithm;
	public MutationAlgorithm mutation_algorithm;
	
	public GeneticAlgorithmData getCopy()
	{
		GeneticAlgorithmData copy = new GeneticAlgorithmData();
		copy.poblation_size = poblation_size;
		copy.max_gen_num = max_gen_num;
		copy.cross_chance = cross_chance;
		copy.mutation_chance = mutation_chance;
		copy.tolerance = tolerance;
		copy.elitism_percentage = elitism_percentage;
		copy.maximize = maximize;
		copy.dimensions = dimensions;
		copy.inicializationType = inicializationType;
		copy.chromosome_factory = chromosome_factory;
		copy.selection_algorithm = selection_algorithm;
		copy.cross_algorithm = cross_algorithm;
		copy.mutation_algorithm = mutation_algorithm;
		return copy;
	}
}
