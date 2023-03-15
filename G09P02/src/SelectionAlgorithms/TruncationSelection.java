/**
 * 
 */
package SelectionAlgorithms;

import java.util.Arrays;

import Chromosomes.Chromosome;

/**
 * @author Rioni
 *  Selecciona el x% de la población y lo duplica 1/x veces, eliminando la población con mala puntuación.
 */
public class TruncationSelection extends SelectionAlgorithm {
	static double trunc = 0.5; // [0.1, 0.5]
	
	@Override
	public Chromosome[] select(Chromosome[] poblation, int poblation_size) {
		// ORDER POBLATION
		Arrays.sort(poblation);
		
		Chromosome[] new_population = new Chromosome[poblation_size];
		
		// 0.3 * 8 eltos = 2.4 ==> i < 2 ==> 2 eltos
		int index = 0;
		double num_padres = (trunc * poblation.length);
		for (int i = 0; i < num_padres; i ++) { 
			for (int j = 0; j < 1 / trunc; j++) {
				if(index < poblation_size) {
					new_population[index] = poblation[i].getCopy();
					index += 1;
				}
			}
		}
		
		// En caso de que no se haya llenado entero la poblacion rellenamos con los mejores.
		int padre = 0;
		while (index < poblation_size) {
			new_population[index] = poblation[padre].getCopy();
			index++;
			if (padre < (int)num_padres) padre++;
			else padre = 0;
		}
		
		return new_population;
	}

}
