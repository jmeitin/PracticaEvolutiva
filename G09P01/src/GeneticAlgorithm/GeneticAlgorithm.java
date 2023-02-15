package GeneticAlgorithm;

public class GeneticAlgorithm<T, U> {
	private Chromosome<T, U>[] poblation; //clase individuo?
	private double[] fitness;//NICO NO LO USA?
	
	//PROBABILIDADES-------
	private double cross_chance;
	private double mutation_chance;
	
	// TAMANYOS------------
	private int max_gen_num; //max generaciones
	private int poblation_size;
	private int tamTorneo; //NICO NO LO USA?
	
	private Chromosome<T, U> best_chromosome;
	private int best_pos;
	
	double tolerance;
	double best_absolute_fitness;
	boolean maximize;
	int dimensions;
	ChromosomeFactory<T, U> chromosomeFactory;

	public GeneticAlgorithm(
			int poblation_size, int max_gen_num, double cross_chance, double mutation_chance,
			double tolerance, boolean maximize, int dimensions, ChromosomeFactory<T, U> chromosomeFactory) {
		//POBLACION
		this.poblation = new Chromosome[poblation_size];
		//TAMANYOS
		this.poblation_size = poblation_size;		
		this.max_gen_num = max_gen_num;
		//PROBABILIDADES
		this.cross_chance = cross_chance;
		this.mutation_chance = mutation_chance;
		this.tolerance = tolerance;
		this.maximize = maximize;
		this.best_absolute_fitness = maximize ? Double.MIN_VALUE : Double.MAX_VALUE;
		this.dimensions = dimensions;
		this.chromosomeFactory = chromosomeFactory;
		
	}
	
	public GeneticAlgorithm(GeneticAlgorithmData algorithmData) {
		//POBLACION
		this.poblation = new Chromosome[algorithmData.poblation_size];
		//TAMANYOS
		this.poblation_size = algorithmData.poblation_size;		
		this.max_gen_num = algorithmData.max_gen_num;
		//PROBABILIDADES
		this.cross_chance = algorithmData.cross_chance;
		this.mutation_chance = algorithmData.mutation_chance;
		this.tolerance = algorithmData.tolerance;
		this.maximize = algorithmData.maximize;
		this.best_absolute_fitness = algorithmData.maximize ? Double.MIN_VALUE : Double.MAX_VALUE;
		this.dimensions = algorithmData.dimensions;
		this.chromosomeFactory = algorithmData.chromosomeFactory;
		
	}

	public void initializePoblation() {
		for (int i = 0; i < poblation_size; i++)
			poblation[i] = createChromosome();
	}

	// Shorthand to ChromosomeFactory
	private Chromosome<T, U> createChromosome() {
		return chromosomeFactory.createChromosome(tolerance, dimensions);
	}
	
	public void run()
	{
		System.out.println("Run GeneticAlgorithm");
	}
}
