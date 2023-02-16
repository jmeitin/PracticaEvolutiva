package SelectionAlgorithms;

import java.util.List;
import java.util.Random;

import GeneticAlgorithm.Chromosome;

public class RouletteSelection extends SelectionAlgorithm {

	@Override
	public Chromosome[] select(Chromosome[] poblation, int poblation_size) {
		int[] sel_super = new int[poblation_size];
		double prob;
		Random rnd = new Random();
		int pos_super;
		
		for(int i = 0; i < poblation_size; i++)
		{
			pos_super = 0;
			prob = rnd.nextDouble();
			while(prob > poblation[pos_super].getAccumulatedScore() && pos_super < poblation_size) pos_super++;
			sel_super[i] = pos_super;
		}
		
		Chromosome[] newPob = new Chromosome[poblation_size];
		for(int i = 0; i < poblation_size; i++) newPob[i] = poblation[sel_super[i]];
		for(int i = 0; i < poblation_size; i++) poblation[i] = newPob[i].getCopy();
		return poblation;
	}

}
