package ru.otus.list;

import org.junit.Assert;
import org.junit.Test;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by egor on 29.07.17.
 */
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
        IntStream.range(0, 100).map(i -> 99 - i).boxed().forEach(i -> Assert.assertEquals(i, myList.get(i)));
    }

    @Test
    public void copyTest() {
        //given
        List<Integer> myList = new MyArrayList<>();
        IntStream.range(0, 100).forEach(myList::add);
        List<Integer> dest = new ArrayList<>();
        List<Integer> src = new MyArrayList<>();

        //when
        Collections.copy(src, myList);
        Collections.copy(dest, src);

        //then
        IntStream.range(0, 100).boxed().forEach(i -> Assert.assertTrue(i.equals(myList.get(i)) &&
                                                                                i.equals(src.get(i)) &&
                                                                                i.equals(dest.get(i))));
    }

}
