package CrossAlgorithms.P2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Chromosomes.ChromosomeP2;


/*
 * Se ordenan las ciudades en una lista dinámica de referencia ascendente. 
 * Para construir un individuo se van sacando una a una las ciudades recorridas, 
 * codificando en el j-ésimo gen del individuo la posición que tiene la j-ésima ciudad en la lista dinámica 
 * (esto es la codificación ordinal en sí. Ahora podemos aplicar cruce monopunto. 
 * Tras eso se realiza la decodificación (que consiste en usar esa lista dinámica y volver a codificarla usando el gen actual tras el cruce monopunto.
*/

public class COCross extends CrossAlgorithmsP2 {

	@Override
	protected void cross(ChromosomeP2 first_child, ChromosomeP2 second_child) {
		// CALCULATE POSITIONS ARRAY		
		int[] positions1 = GetPositionsInDynamicList(first_child);
		int[] positions2 = GetPositionsInDynamicList(second_child);
		
		// ONE POINT CROSS
		Random rand = new Random();
		int num_genes = first_child.getNumOfGenes();
		int cut_point =  1 + (int)(rand.nextDouble() * (num_genes - 2));
		
		// SWAP POSITIONS
		for (int i = cut_point; i < num_genes; i++) {
			int aux = positions1[i];
			positions1[i] = positions2[i];
			positions2[i] = aux;
		}
		
		// PARSE POSITIONS IN DYNAMIC LIST TO GENES
		int[] genes1 = ConvertPositionsToGenes(positions1);
		int[] genes2 = ConvertPositionsToGenes(positions2);
		
		first_child.setGenes(genes1);
		second_child.setGenes(genes2);
	}
	
	/*
	 * */
	private List<Integer> InitializeDynamicList(int num_genes){
		List<Integer> values = new ArrayList<Integer>();
		
		for(int i = 0; i < num_genes; i++) {
			values.add(i);
		}
		
		return values;
	}
	
	/*
	 * CALCULATES POS OF EACH CITY IN DYNAMIC LIST
	 * */
	private int[] GetPositionsInDynamicList(ChromosomeP2 chromosome) {
		int num_genes = chromosome.getNumOfGenes();
		List<Integer> values = InitializeDynamicList(num_genes);
		
		// DETERMINE POS IN genes
		int[] positions = new int[num_genes];
		int[] genes = chromosome.getGenesCopy();
		for (int i = 0; i < genes.length; i++) {
			int value = genes[i];
			
			// SEARCH FOR CURRENT POS IN LIST
			int pos = 0;
			while (pos < values.size() && values.get(pos) != value)
				pos++;
			
			//NO ESTOY SEGURO DE SI HABRIA QUE SEPARAR en caso pos < size
			positions[i] = pos;
			values.remove(pos);
		}
		
		return positions;
	}

	/*
	 * */
	private int[] ConvertPositionsToGenes(int[] positions) {
		int num_genes = positions.length;
		List<Integer> values = InitializeDynamicList(num_genes);
		int[] genes = new int[num_genes];
		
		for(int i = 0; i < num_genes; i++) {
			int pos = positions[i];
			
			// SEARCH FOR CURRENT POS IN LIST
			int value = 0;
			while (value < values.size() && values.get(value) != pos)
				value++;
			//NO ESTOY SEGURO DE SI HABRIA QUE SEPARAR en caso value < size
			genes[i] = value;
			values.remove(value);
		}
		
		return genes;			
	}
}
