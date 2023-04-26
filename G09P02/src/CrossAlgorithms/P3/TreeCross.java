package CrossAlgorithms.P3;

import Chromosomes.BinaryTree;
import Chromosomes.Chromosome;
import Chromosomes.ChromosomeP3;
import CrossAlgorithms.CrossAlgorithm;

public class TreeCross extends CrossAlgorithm {

	@Override
	protected void cross(Chromosome first_child, Chromosome second_child) {

		ChromosomeP3 parentA = (ChromosomeP3) first_child;
		ChromosomeP3 parentB = (ChromosomeP3) second_child;

		BinaryTree treeA = parentA.getTreeCopy();
		BinaryTree treeB = parentB.getTreeCopy();
		// Los subarboles se seleccionan con una probabilidad de 0.9 (dr) & 0.1 (iz)
		BinaryTree subA = null;
		BinaryTree subB = null;

		while (subA == null || subB == null) {
			subA = treeA.getRandomNode(0.9);
			subB = treeB.getRandomNode(0.1);

			if (subA != null && subB != null) {
				BinaryTree aux = subA.getCopy();
				subA.copyTree(subB);
				subB.copyTree(aux);
			}

			parentA.setTree(treeA);
			parentB.setTree(treeB);
		}
		
		parentA.setTree(treeA);
		parentB.setTree(treeB);		
	}
}
