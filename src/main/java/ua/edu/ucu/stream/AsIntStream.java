package ua.edu.ucu.stream;

import ua.edu.ucu.function.*;

import java.util.*;

public class AsIntStream implements IntStream {
    ArrayList<Integer> innerCol;
    private Iterator<Integer> iterator;
    
    private AsIntStream(Iterator<Integer> iter) {
        // this.innerCol = new ArrayList<>();
        // iter.forEachRemaining(innerCol::add);
        this.iterator = iter;
    }

    public Iterator<Integer> getIterator() {
        return this.iterator;
    }

    public static IntStream of(int... values) {
        IntStream stream = new AsIntStream(addItems(values));
        return stream;
    }

    @Override
    public Double average() {
        checkInnerCol();
        return (double) sum()/count();
    }

    @Override
    public Integer max() {
        checkInnerCol();
        int res = Integer.MIN_VALUE;
        for (int i: this.innerCol) {
            if (i > res) {
                res = i;
            }
        }
        return res;
    }

    @Override
    public Integer min() {
        checkInnerCol();
        int res = Integer.MAX_VALUE;
        for (int i: this.innerCol) {
            if (i < res) {
                res = i;
            }
        }
        return res;
    }

    @Override
    public long count() {
        return this.innerCol.size();
    }

    @Override
    public Integer sum() {
        int res = 0;
//        for (int i: this.innerCol) {
//            res += i;
//        }
        while (this.iterator.hasNext()) {
            res += iterator.next();
        }
        return res;
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        Iterable<Integer> filtered = () -> new FilterIterator(predicate);

        return new AsIntStream(filtered.iterator());
    }

    @Override
    public void forEach(IntConsumer action) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        Iterable<Integer> mapped = () -> new MapIterator(mapper);

        return new AsIntStream(mapped.iterator());
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        Iterable<Integer> flatted = () -> new FlatMapIterator(func);

        return new AsIntStream(flatted.iterator());
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int[] toArray() {
       ArrayList<Integer> innerCol = new ArrayList<>();
       while (this.iterator.hasNext()) {
           innerCol.add(this.iterator.next());
       }
       int[] res = new int[innerCol.size()];
       for (int i = 0; i < innerCol.size(); i++) {
           res[i] = innerCol.get(i);
       }
       return res;
    }

    private static Iterator<Integer> addItems(int... values) {
        ArrayList<Integer> temp = new ArrayList<>();
        for (int i: values) {
            temp.add(i);
        }
        return temp.iterator();
    }

    private void checkInnerCol() {
        if (this.innerCol.isEmpty()) {
            throw new IllegalArgumentException("Stream is empty!");
        }
    }

    private class FilterIterator implements Iterator<Integer>{
        private IntPredicate predicate;
        private int counter = 0;

        FilterIterator(IntPredicate predicate) {
            this.predicate = predicate;
        }

        @Override
        public boolean hasNext() {
            // return counter < innerCol.size();
            return iterator.hasNext();
        }

        @Override
        public Integer next() {
            do {
                // int temp = innerCol.get(counter);
                int temp = iterator.next();
                counter++;
                if (predicate.test(temp)) {
                    return temp;
                }
            } while (hasNext());
            return null;
        }
    }

    private class MapIterator implements Iterator<Integer>{
        private IntUnaryOperator operator;

        MapIterator(IntUnaryOperator operator) {
            this.operator = operator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Integer next() {
            int temp = iterator.next();
            return operator.apply(temp);
        }
    }

    private class FlatMapIterator implements Iterator<Integer> {
        private IntToIntStreamFunction streamFunction;
        private ArrayList<Integer> meta;
        private Iterator<Integer> metaIterator;

        FlatMapIterator(IntToIntStreamFunction streamFunction) {
            this.streamFunction = streamFunction;
            this.meta = new ArrayList<>();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext() || this.metaIterator.hasNext();
        }

        @Override
        public Integer next() {
            if (iterator.hasNext()) {
                IntStream temp = this.streamFunction.applyAsIntStream(iterator.next());
                for (int i: temp.toArray()) {
                    this.meta.add(i);
                }
                this.metaIterator = this.meta.iterator();
            }
            return this.metaIterator.next();
        }
    }
}
