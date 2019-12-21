package ua.edu.ucu.stream;

import ua.edu.ucu.function.*;

import java.lang.reflect.Array;
import java.util.*;

public class AsIntStream implements IntStream {
    private Integer counter;
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
        long sample = 0;
        this.counter = 0;
        while (this.iterator.hasNext()) {
            sample += this.iterator.next();
            this.counter++;
        }
        if (this.counter==0) {
            return 0.0;
        } else {
            return (double) sample/this.counter;
        }
    }

    @Override
    public Integer max() {
        checkEmptyStream();
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
        checkEmptyStream();
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
        if (this.counter != null) {
            return this.counter;
        }
        this.counter = 0;
        while (iterator.hasNext()) {
            iterator.next();
            this.counter++;
        }
        return this.counter;
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
        checkEmptyStream();
        ArrayList<Integer> filtered = new ArrayList<>();
        while (iterator.hasNext()) {
            int el = iterator.next();
            if (predicate.test(el)) {
                filtered.add(el);
            }
        }

        return new AsIntStream(filtered.iterator());
    }

    @Override
    public void forEach(IntConsumer action) {
        checkEmptyStream();
        while (this.iterator.hasNext()) {
            action.accept(this.iterator.next());
        }
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        checkEmptyStream();
        Iterable<Integer> mapped = () -> new MapIterator(mapper);

        return new AsIntStream(mapped.iterator());
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        checkEmptyStream();
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

    private void checkEmptyStream() {
        if (!this.iterator.hasNext()) {
            throw new IllegalArgumentException("Stream is empty!");
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
            this.metaIterator = Collections.emptyIterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext() || this.metaIterator.hasNext();
        }

        @Override
        public Integer next() {
            while (iterator.hasNext()) {
                int next = iterator.next();
                IntStream temp = this.streamFunction.applyAsIntStream(next);
                for (int i: temp.toArray()) {
                    this.meta.add(i);
                }
                this.metaIterator = this.meta.iterator();
            }
            return this.metaIterator.next();
        }
    }
}
