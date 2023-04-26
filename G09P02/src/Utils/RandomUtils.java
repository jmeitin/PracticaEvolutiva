package Utils;

import java.math.BigDecimal;
import java.math.MathContext;

public class RandomUtils {

	// Random number between 0 and max (both inclusive)
	public static int getRandomInt(int max) {
		return (int) (Math.random() * (max + 1));
	}

	// Random number between min and max (both inclusive)
	public static int getRandomInt(int min, int max) {
		return (int) (Math.random() * (max - min + 1) + min);
	}
	
	// Random double between min and max (both inclusive)
	public static double getRandomDouble(double min, double max) {
		return (Math.random() * (max - min + 1) + min);
	}

	// Probabilty (double between 0 and 100)
	public static boolean getProbability(double probability) {
		return Math.random() * 100 < probability;
	}
}
