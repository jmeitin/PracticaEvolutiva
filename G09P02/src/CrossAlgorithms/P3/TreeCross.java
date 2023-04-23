package CrossAlgorithms.P3;

import Chromosomes.BinaryTree;
import Chromosomes.Chromosome;
import Chromosomes.ChromosomeP3;
import CrossAlgorithms.CrossAlgorithm;


public class TreeCross extends CrossAlgorithm {

	@Override
	protected void cross(Chromosome first_child, Chromosome second_child) {
		
		ChromosomeP3 parentA = (ChromosomeP3)first_child;
		ChromosomeP3 parentB = (ChromosomeP3)first_child;
		
		BinaryTree subA = parentA.getTree().getRandomNode(0.9);
		BinaryTree subB = parentB.getTree().getRandomNode(0.1);
		
		if(subA != null && subB != null) {
			BinaryTree aux = subA.getCopy();
			
			subA.copyTree(subB);
			subB.copyTree(aux);
		}
		
		first_child = parentA.getCopy();
		second_child = parentB.getCopy();
		
	}
}
