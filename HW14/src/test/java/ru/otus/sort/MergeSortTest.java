package ru.otus.sort;


import org.junit.Assert;
import org.junit.Test;

import java.util.stream.IntStream;

public class MergeSortTest {

    @Test
    public void shouldSortArrayWithComparator() {
        //given
        Integer[] array = IntStream.range(0, 100)
                .map(i -> (int)(1000 * Math.random()))
                .boxed()
                .toArray(Integer[]::new);

        //when
        Integer[] sorted = SortUtil.sort(array, (i1, i2) -> Integer.compare(i2, i1));

        //then
        IntStream.range(0, 99).forEach(i -> {
            Assert.assertTrue(sorted[i] >= sorted[i + 1]);
        });
    }

    @Test
    public void shouldSortArray() {
        //given
        Integer[] array = IntStream.range(0, 100)
                .map(i -> (int)(1000 * Math.random()))
                .boxed()
                .toArray(Integer[]::new);

        //when
        Integer[] sorted = SortUtil.sort(array);

        //then
        IntStream.range(0, 99).forEach(i -> {
            Assert.assertTrue(sorted[i] <= sorted[i + 1]);
        });
    }

}
