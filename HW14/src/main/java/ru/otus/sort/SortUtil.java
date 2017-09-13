package ru.otus.sort;

import java.util.Comparator;
import java.util.concurrent.ForkJoinPool;

public class SortUtil {

    public static <T> T[] sort(T[] array, Comparator<T> comparator) {
        return ForkJoinPool.commonPool().invoke(new MergeSortTask<>(array, comparator));
    }

    public static <T extends Comparable<T>> T[] sort(T[] array) {
        return sort(array, null);
    }

    private SortUtil(){
    }
}
