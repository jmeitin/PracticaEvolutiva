package View;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RangedValue<T extends Number> implements Iterable<T> {
    public T min_value;
    public T max_value;
    private final int num_steps;

    public RangedValue(T min_value, T max_value, int num_steps) {
        this.min_value = min_value;
        this.max_value = max_value;
        this.num_steps = num_steps;
    }
    
    public RangedValue(int num_steps) {
        this.num_steps = num_steps;
    }

    public int getNumSteps()
    {
    	return num_steps;
    }
    
    public Iterator<T> iterator() {
        return new RangedValueIterator();
    }

    private class RangedValueIterator implements Iterator<T> {
        private double step;
        private double current;
        private int remainingSteps;

        public RangedValueIterator() {
            step = (max_value.doubleValue() - min_value.doubleValue()) / (double) (num_steps - 1);
            current = min_value.doubleValue();
            remainingSteps = num_steps;
        }

        public boolean hasNext() {
            return remainingSteps > 0;
        }

        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            T value = null;
            if (min_value instanceof Integer) {
                value = (T) (Integer) (int) current;
            } else if (min_value instanceof Double) {
                value = (T) (Double) current;
            } // Añade más casos según los tipos que quieras aceptar

            current += step;
            remainingSteps--;

            return value;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}