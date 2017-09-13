package ru.otus.sort;

import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.RecursiveTask;

public class MergeSortTask<T> extends RecursiveTask<T[]> {

    private T[] array;
    private Comparator<T> cmp;

    @SuppressWarnings("unchecked")
    MergeSortTask(T[] array, Comparator<T> cmp) {
        Objects.requireNonNull(array);
        this.array = array;
        this.cmp = cmp != null ? cmp : (t1, t2) -> ((Comparable<? super T>) t1).compareTo(t2);
    }

    @Override
    protected T[] compute() {
        if (array.length > 1) {
            Pair<T[], T[]> leftAndRight = divide(array);
            MergeSortTask<T> left = new MergeSortTask<>(leftAndRight.getKey(), cmp);
            MergeSortTask<T> right = new MergeSortTask<>(leftAndRight.getValue(), cmp);
            invokeAll(left, right);
            return merge(left.join(), right.join());
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    private T[] merge(T[] left, T[] right) {
        int i = 0, j = 0, k = 0;
        T[] mergedArray = (T[]) Array.newInstance(left.getClass().getComponentType(), left.length + right.length);

        while ((i < left.length) && (j < right.length)) {
            mergedArray[k++] = cmp.compare(left[i], right[j]) < 0 ? left[i++] : right[j++];
        }

        if (i == left.length) {
            for (int a = j; a < right.length; a++) {
                mergedArray[k++] = right[a];
            }
        } else {
            for (int a = i; a < left.length; a++) {
                mergedArray[k++] = left[a];
            }
        }

        return mergedArray;
    }

    private Pair<T[], T[]> divide(T[] array) {
        return new Pair<>(Arrays.copyOfRange(array, 0, array.length / 2),
                          Arrays.copyOfRange(array, array.length / 2, array.length));
    }
}
