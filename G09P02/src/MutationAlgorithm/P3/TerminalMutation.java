package MutationAlgorithm.P3;

import Chromosomes.Chromosome;
import Chromosomes.ChromosomeP3;
import MutationAlgorithm.MutationAlgorithm;

/* 
 *  Se selecciona al azar un símbolo terminal dentro del individuo,
y se sustituye por otro diferente del conjunto de símbolos terminales posibles 
*/

public class TerminalMutation extends MutationAlgorithm {

	@Override
	public Chromosome[] mutate(Chromosome[] poblation, int poblation_size, double mutation_chance){
		//System.out.println("=== MUTACION TERMINAL ===");
		Chromosome[] new_population = new Chromosome[poblation_size];
		
		for (int i = 0; i < poblation_size; i++) {
			ChromosomeP3 chromosome = (ChromosomeP3) poblation[i].getCopy();
			
			//Mutate
			double d = rand.nextDouble();
			if(d <= mutation_chance)
				chromosome.mutateTerminal();			
			
			new_population[i] = chromosome.getCopy();
		}
		return new_population;	
	}
}
