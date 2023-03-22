package GeneticAlgorithm;

import Chromosomes.ChromosomeFactory;
import CrossAlgorithms.P1.CrossAlgorithm;
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
	public ChromosomeFactory chromosome_factory;
	public SelectionAlgorithm selection_algorithm;
	public CrossAlgorithm cross_algorithm;
	public MutationAlgorithm mutation_algorithm;
}
