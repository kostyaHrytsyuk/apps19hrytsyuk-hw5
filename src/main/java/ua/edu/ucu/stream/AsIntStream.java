package ua.edu.ucu.stream;

import ua.edu.ucu.function.*;

import java.util.*;

public class AsIntStream implements IntStream {
    ArrayList<Integer> innerCol;
    private Iterator<Integer> iterator;
    
    private AsIntStream(Iterator<Integer> iter) {
        this.iterator = iter;
    }

    public static IntStream of(int... values) {
        return new AsIntStream(addItems(values));
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
        while (this.iterator.hasNext()) {
            action.accept(this.iterator.next());
        }
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
        while (this.iterator.hasNext()) {
            identity = op.apply(identity, this.iterator.next());
        }

        return identity;
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
        ArrayList<Integer> innerCol = new ArrayList<>();
        for (int i: values) {
            innerCol.add(i);
        }
        return innerCol.iterator();
    }

    private void checkInnerCol() {
        if (this.innerCol.isEmpty()) {
            throw new IllegalArgumentException("Stream is empty!");
        }
    }

    private class FilterIterator implements Iterator<Integer>{
        private IntPredicate predicate;

        FilterIterator(IntPredicate predicate) {
            this.predicate = predicate;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Integer next() {
            do {
                int temp = iterator.next();
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
            return operator.apply(iterator.next());
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
