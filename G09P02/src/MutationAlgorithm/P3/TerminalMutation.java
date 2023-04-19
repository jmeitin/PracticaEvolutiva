package MutationAlgorithm.P3;

import Chromosomes.Chromosome;
import Chromosomes.ChromosomeP3;
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

public class TerminalMutation extends MutationAlgorithm {

	@Override
	public Chromosome[] mutate(Chromosome[] poblation, int poblation_size, double mutation_chance){
		//System.out.println("=== MUTACION ORIGINAL ===");
		Chromosome[] new_population = new Chromosome[poblation_size];
		
		int num_genes = poblation[0].getNumOfGenes();
		
		for (int i = 0; i < poblation_size; i++) {
			ChromosomeP3 chromosome = (ChromosomeP3) poblation[i].getCopy();
			
			//Mutate
			
			
			new_population[i] = chromosome.getCopy();
		}
		return new_population;	
	}
	
	//int allelePoint = calculateNextPoint(rand, 1 * alleleLength, (length - 1) * alleleLength);
}
