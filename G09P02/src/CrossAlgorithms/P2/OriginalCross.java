package CrossAlgorithms.P2;

import java.util.ArrayList;
import java.util.List;

import Chromosomes.ChromosomeP2;

/*
Algoritmo de cruce propio.
Se genera una lista dinamica con todos los valores posibles que puede tener un gen ordenados de menor a mayor.
Luego, se recorre la lista de genes comparando los valores de los 2 progenitores.
Se intenta meter en el primer individuo el menor valor si este aun no se ha usado (es decir, se encuentra aun en la lista dinamica).
Si no, se mete el primer elemento de la lista dinamica.
En el segundo individuo es el reves. Intenta meter el máximo.
 * */
public class OriginalCross extends CrossAlgorithmsP2 {

	@Override
	protected void cross(ChromosomeP2 first_child, ChromosomeP2 second_child) {
		
		int num_genes = first_child.getNumOfGenes();
		int[] genes1 = first_child.getGenesCopy();
		int[] genes2 = second_child.getGenesCopy();
		
		List<Integer> genes_not_used1 = new ArrayList<Integer>();
		List<Integer> genes_not_used2 = new ArrayList<Integer>();
		
		for(int i = 0; i < num_genes; i++) {
			genes_not_used1.add(i);
			genes_not_used2.add(i);
		}
		
		int[] solution1 = new int[num_genes];
		int[] solution2 = new int[num_genes];
		
		for(int i = 0; i < num_genes; i++) {
			int min = Math.min(genes1[i], genes2[i]);
			int max = Math.max(genes1[i], genes2[i]);
			
			// FIRST CHILD
			SetGeneInCross(genes_not_used1, solution1, i, min);

			//SECOND CHILD----------------------------------
			SetGeneInCross(genes_not_used2, solution2, i, max);
		}
		
		first_child.setGenes(solution1);
		second_child.setGenes(solution2);
	}
	
	private void SetGeneInCross(List<Integer> genes_not_used, int[] solution, int i, int num) {
		if(genes_not_used.contains(num)) {
			solution[i] = num;
			genes_not_used.remove(num);
		}
		else {
			solution[i] = genes_not_used.get(0);
			genes_not_used.remove(solution[i]);
		}
	}
}