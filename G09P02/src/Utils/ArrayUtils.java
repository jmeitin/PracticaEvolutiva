package Utils;

public class ArrayUtils {
	public static int indexOf(int[] array, int element) {
	    for (int i = 0; i < array.length; i++) {
	        if (array[i] == element) {
	            return i;
	        }
	    }
	    // Si el elemento no se encuentra en el array, devuelve -1
	    return -1;
	}
}
