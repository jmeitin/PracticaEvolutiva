package Chromosomes;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;

import Utils.ArrayUtils;

public class ChromosomeP3 extends Chromosome<Integer, String> {
	
	Random rand = new Random();
	private BinaryTree tree;
	private String fenotype;
	private String creation_type = "";
	double[][] dataset = new double[100][2];
	
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

	public String getCreationType() {
		return creation_type;
	}
	
	@Override
	protected void initGenes() {
		
		// Aqui inicializamos el dataset		
		double stepSize = 2.0 / 100;

		for (int i = 0; i < 100; i++) {
		    double x = -1.0 + i * stepSize;
		    x = Math.round(x * 100.0) / 100.0; // Redondea x a 2 decimales
		    double y = getCorrectValue(x); // Reemplaza esto con la función que has generado dinámicamente
		    dataset[i] = new double[]{x, y};
		}
	}
	
	@Override
	public void calculateFenotypes() {
	
	}
	
	
	public String getFunctionString()
	{
		return ArrayUtils.arrayToMathExpression(tree.toArray()) + "\n" + tree.toArray() + "\nFitness bruto: " + evaluate();
	}
	
	double getCorrectValue(double x)
	{
		return Math.pow(x, 4) + Math.pow(x, 3) + Math.pow(x, 2) + x + 1;
	}

	public Function<Double, Double> getEstimatedFunction()
	{
		return tree.getFunction();
	}
	
	public Function<Double, Double> getRealFunction()
	{
		return  x -> Math.pow(x, 4) + Math.pow(x, 3) + Math.pow(x, 2) + x + 1;
	}
	
	@Override
	public double evaluate() {		
		
		Function<Double, Double> func = tree.getFunction();
		brute_fitness = 0;
		final int iterations = 100;
		
		for(int i = 0; i < iterations;i++)
		{
			final double estimated_value = func.apply(dataset[i][0]);
			final double real_value = dataset[i][1];
			final double cuadratic_difference = Math.pow(real_value - estimated_value, 2);
			
			brute_fitness += cuadratic_difference;
		}
		
		
		brute_fitness /= iterations;
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
	
	public BinaryTree getTreeCopy() {
		return tree.getCopy();
	}
	public BinaryTree getTree() {
		return tree;
	}
	public void setTree(BinaryTree aux) {
		tree = aux;
	}
}
