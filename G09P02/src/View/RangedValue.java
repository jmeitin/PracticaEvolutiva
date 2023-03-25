package View;

import java.util.Iterator;

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
        
        public RangedValueIterator() {
            step = (max_value.doubleValue() - min_value.doubleValue()) / (double) (num_steps - 1);
            current = min_value.doubleValue();
        }
        
        public boolean hasNext() {
            return current <= max_value.doubleValue();
        }

        public T next() {
            T value = (T) (Double) current;
            current += step;
            return value;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}