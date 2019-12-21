package ua.edu.ucu.stream;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AsIntStreamTest {
    private IntStream intStream;
    private int[] intArr = {-20, -10, 10, 20, 30};

    @Before
    public void setUp() {
        this.intStream = AsIntStream.of(intArr);
    }

    @Test
    public void testAverage() {
        double expectedValue = 6;

        assertEquals(expectedValue, this.intStream.average(), Integer.MIN_VALUE);
    }

    @Test
    public void testMax() {
        Integer expectedValue = 30;

        assertEquals(expectedValue, this.intStream.max());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxWithEmptyStream() {
        AsIntStream.of().max();
    }

    @Test
    public void testMin() {
        Integer expectedValue = -20;

        assertEquals(expectedValue, this.intStream.min());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinWithEmptyStream() {
        AsIntStream.of().min();
    }

    @Test
    public void testCount() {
        long expectedValue = 5;

        assertEquals(expectedValue, this.intStream.count());
    }

    @Test
    public void testSum() {
        Integer expectedValue = 30;

        assertEquals(expectedValue, this.intStream.sum());
    }

    @Test
    public void testFilter() {
        int[] expectedValue = {10, 20, 30};

        assertArrayEquals(expectedValue, this.intStream.filter(x -> x > 0).toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterWithEmpty() {
        AsIntStream.of().filter(x -> x > 0);
    }

    @Test
    public void testMap() {
        int[] expectedValue = {400, 100, 100, 400, 900};

        assertArrayEquals(expectedValue, this.intStream.map(x -> x * x).toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapWithEmpty() {
        AsIntStream.of().map(x -> x / 2*x);
    }

    @Test
    public void testFlatMap() {
        int[] expectedValue = {0, 10, 10, 20, 30, 40, 40, 50, 50, 60};

        assertArrayEquals(expectedValue, this.intStream.flatMap(x -> AsIntStream.of(x+20, x+30)).toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatMapWithEmpty() {
        AsIntStream.of().flatMap(x -> AsIntStream.of(x*x));
    }

    @Test
    public void testReduce() {
        int expectedValue = 2400000;

        assertEquals(expectedValue, this.intStream.reduce(2, (prod,x) -> prod * x));
    }
}