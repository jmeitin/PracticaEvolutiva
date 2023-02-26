package GeneticAlgorithm;

import SelectionAlgorithms.SelectionAlgorithm;
import CrossAlgorithms.CrossAlgorithm;
import MutationAlgorithm.MutationAlgorithm;

public class GeneticAlgorithmData {
	public int poblation_size;
	public int max_gen_num;
	public double cross_chance;
	public double mutation_chance;
	public double tolerance;
	public boolean maximize;
	public int dimensions;
	public int tournament_size;
	public ChromosomeFactory chromosomeFactory;
	public SelectionAlgorithm selectionAlgorithm;
	public CrossAlgorithm crossAlgorithm;
	public MutationAlgorithm mutationAlgorithm;
}
