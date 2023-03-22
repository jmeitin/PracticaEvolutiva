package Utils;

public class RandomUtils {

	// Random number between 0 and max (both inclusive)
	public static int getRandomInt(int max) {
		return (int) (Math.random() * (max + 1));
	}

	// Random number between min and max (both inclusive)
	public static int getRandomInt(int min, int max) {
		return (int) (Math.random() * (max - min + 1) + min);
	}
}
