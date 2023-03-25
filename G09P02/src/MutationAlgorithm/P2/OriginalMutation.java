package MutationAlgorithm.P2;

import Chromosomes.Chromosome;
import Chromosomes.ChromosomeP2;
import MutationAlgorithm.MutationAlgorithm;
import Utils.RandomUtils;

/* Algoritmo de Mutación original propio:
 * Se calcula un número aleatorio para cada gen y si este es > mutation_chance se muta.
 * La mutación tiene 2 fases:
 * Fase 1: intercambiar ese gen con otro que se encuentre a X posiciones de distancia.
 * (X es un número aleatorio de [1, num_genes])
 * Fase 2: Todos los genes se desplzan 1 posicion a la derecha.
 * 
 * Ejemplo:
 * Individuo: 0, 1, 2, 3, 4.
 * Fase 1:
 * Se quiere mutar el 1 y se calcula que X = 3. El gen a 3 posiciones de distancia del 1 es el 4.
 * El nuevo Individuo sería 0, 4, 2, 3, 1 
 * Fase 2:
 * 1, 0, 4, 2, 3
*/

public class OriginalMutation extends MutationAlgorithm {

	@Override
	public Chromosome[] mutate(Chromosome[] poblation, int poblation_size, double mutation_chance){
		System.out.println("=== MUTACION ORIGINAL ===");
		Chromosome[] new_population = new Chromosome[poblation_size];
		
		int num_genes = poblation[0].getNumOfGenes();
		
		for (int i = 0; i < poblation_size; i++) {
			ChromosomeP2 chromosome = (ChromosomeP2) poblation[i].getCopy();
			
			int[] genes = chromosome.getGenesCopy();
			boolean has_mutated = false;
			
			for(int g = 0; g < num_genes; g++) {
				//will mutate
				if(RandomUtils.getProbability(mutation_chance)) {
					has_mutated = true;
					//calculate other pos
					int pos = 1 + (int)(rand.nextDouble() * num_genes - 1);
					pos = pos % num_genes;
					//exchange genes
					int aux = genes[pos];
					genes[pos] = genes[g];
					genes[g] = aux;
					
					//displace genes to the right
					genes[0] = genes[num_genes - 1];
					for (int j = 1; j < num_genes; j++) {
					    aux = genes[j];
					    genes[j] = genes[j-1];
					    genes[j-1] = aux;
					}
				}
			}			
			if(has_mutated)
				chromosome.setGenes(genes);
			
			new_population[i] = chromosome;
		}

		System.out.println("=== FIN MUTACION ORIGINAL ===");
		return new_population;	
	}
	
	//int allelePoint = calculateNextPoint(rand, 1 * alleleLength, (length - 1) * alleleLength);
}
