package CrossAlgorithms.P2;

import Chromosomes.Chromosome;
import Chromosomes.ChromosomeP2;
import CrossAlgorithms.CrossAlgorithm;

public abstract class CrossAlgorithmsP2 extends CrossAlgorithm {

	@Override
	protected void cross(Chromosome first_child, Chromosome second_child) {
		this.cross((ChromosomeP2)first_child, (ChromosomeP2)second_child);
		

	}

	protected abstract void cross(ChromosomeP2 first_child, ChromosomeP2 second_child);
}
