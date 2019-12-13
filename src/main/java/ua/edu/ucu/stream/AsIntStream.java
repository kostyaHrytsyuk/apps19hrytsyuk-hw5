package ua.edu.ucu.stream;

import ua.edu.ucu.function.*;

import java.util.ArrayList;

public class AsIntStream implements IntStream {
    ArrayList<Integer> innerCol;
    
    private AsIntStream(int... values) {
        this.innerCol = new ArrayList<Integer>();
        addItems(values);
    }

    public static IntStream of(int... values) {
        IntStream stream = new AsIntStream(values);
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
        for (int i: this.innerCol) {
            res += i;
        }
        return res;
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void forEach(IntConsumer action) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int[] toArray() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void addItems(int... values) {
        for (int i: values) {
            this.innerCol.add(i);
        }
    }

    private void checkInnerCol() {
        if (this.innerCol.isEmpty()) {
            throw new IllegalArgumentException("Stream is empty!");
        }
    }
}
