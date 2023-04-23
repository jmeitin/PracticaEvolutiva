package Chromosomes;

import java.util.Random;

public class ChromosomeP3 extends Chromosome<Integer, Integer> {
	
	Random rand = new Random();
	private BinaryTree tree;
	private String fenotype;
	private String creation_type = "";
	
	public ChromosomeP3(double tolerance, int depth, String t) {
		super(10); // ????????????????
		
		//create tree
		tree = new BinaryTree(true); //is root
		
		//Init tree
		creation_type = t;
		
		switch(creation_type){
		case "COMPLETA": 
			tree.FullInitalization(depth);
		break;
		case "CRECIENTE":
			tree.GrowInitalization(depth);
		break;
		default:
			tree.FullInitalization(depth);
		break;
		}
		
		initGenes();
		calculateFenotypes();
	}

	@Override
	protected void initGenes() {
		
		
	}
	
	@Override
	public void calculateFenotypes() {
		
	}
	
	

	@Override
	public double evaluate() {		
		
		return brute_fitness;
	}

	@Override
	protected Chromosome getNewInstance() {
		return new ChromosomeP3(this.tolerance, 5 ,"COMPLETO");
	}

	@Override
	protected boolean mutateGene(int pos, Random rand, double mutation_chance) {
		// TODO Auto-generated method stub
		return false;
	}

	
	// Returns a copy of this Chromosome
	@Override
	public Chromosome getCopy()
	{
		ChromosomeP3 chromosome = (ChromosomeP3) getNewInstance();
		chromosome.setFitness(this.fitness);	
		chromosome.setBruteFitness(this.brute_fitness);
		chromosome.setScore(this.score);
		chromosome.setAccumulatedScore(this.scoreAccumulated);
		chromosome.fenotypes = this.fenotypes;
		chromosome.num_of_genes = this.num_of_genes;
		chromosome.tree = this.tree.getCopy();
		//FALTAN COSAS?????????????????????????????????????????????????????????????????
		
		return chromosome;
	};
	
	// MUTATE==========================================================================
	public void mutateTerminal() {
		if(tree != null) {
			tree.MutateTerminal(tree);
		}
	}
	public void mutateFunction(double mutation_chance) {
		if(tree != null) {
			tree.MutateFunction(tree, mutation_chance);
		}
	}
	public void mutateSubTree(double mutation_chance) {
		if(tree != null) {
			tree.MutateSubTree(tree, mutation_chance);
		}
	}
	
	public BinaryTree getTree() {
		return tree;
	}
}
