package ru.otus.list;

import org.junit.Assert;
import org.junit.Test;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class MyArrayListTest {

    @Test
    public void addAllTest() {
        //given
        List<Integer> myList = new MyArrayList<>();
        Integer[] array = new Integer[100];
        IntStream.range(0, 100).forEach(i -> array[i] = i);

        //when
        boolean result = Collections.addAll(myList, array);

        //then
        Assert.assertTrue(result);
        Assert.assertEquals(100, myList.size());
        IntStream.range(0, 100).boxed().forEach(i -> Assert.assertEquals(i, myList.get(i)));
    }

    @Test
    public void sortTest() {
        //given
        List<Integer> myList = new MyArrayList<>();
        IntStream.range(0, 100).forEach(myList::add);

        //when
        Collections.sort(myList, (i1, i2) -> Integer.compare(i2, i1));

        //then
        IntStream.range(0, 100).boxed().forEach(i -> Assert.assertEquals(Integer.valueOf(99 - i), myList.get(i)));
    }

    @Test
    public void copyTest() {
        //given
        List<Integer> myList = new MyArrayList<>();
        List<Integer> dest = new ArrayList<>(100);
        List<Integer> src = new MyArrayList<>(100);
        IntStream.range(0, 100).forEach(i -> {
            src.add(0);
            dest.add(0);
            myList.add(i);
        });

        //when
        Collections.copy(src, myList);
        Collections.copy(dest, myList);

        //then
        IntStream.range(0, 100).boxed().forEach(i -> Assert.assertTrue(i.equals(myList.get(i)) &&
                                                                                i.equals(src.get(i)) &&
                                                                                i.equals(dest.get(i))));
    }

}
