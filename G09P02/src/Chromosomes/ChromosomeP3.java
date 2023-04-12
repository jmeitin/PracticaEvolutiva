package Chromosomes;

import java.util.Random;

public class ChromosomeP3 extends Chromosome<Integer, Integer> {
	//protected int[] genes;
	
	public static final String terminales[] = { "A0", "A1", "D0", "D1", "D2", "D3" };
	public static final String funciones[] = { "AND", "OR", "NOT", "IF" };
	private BinaryTree tree;
	private String fenotype;
	
	public ChromosomeP3(double tolerance) {
		super(10);
//		arbol = new BinaryTree(profundidad, useIf);
//		switch(tipoCreacion){
//		case 0:
//			arbol.inicializacionCreciente(0);
//		break;
//		case 1:
//			arbol.inicializacionCompleta(0,0);
//		break;
//		case 2:
//			int ini = new Random().nextInt(2);
//			if(ini == 0) 
//				arbol.inicializacionCreciente(0);
//			else arbol.inicializacionCompleta(0,0);
//		break;
//		}
		
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
		return new ChromosomeP2(this.tolerance);
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
		ChromosomeP2 chromosome = (ChromosomeP2) getNewInstance();
		chromosome.setFitness(this.fitness);	
		chromosome.setBruteFitness(this.brute_fitness);
		chromosome.setScore(this.score);
		chromosome.setAccumulatedScore(this.scoreAccumulated);
		chromosome.fenotypes = this.fenotypes;
		chromosome.num_of_genes = this.num_of_genes;
		//chromosome.genes = this.getGenesCopy();
		
		return chromosome;
	};
}
