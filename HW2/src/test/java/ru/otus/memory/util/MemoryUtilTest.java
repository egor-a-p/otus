package ru.otus.memory.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author Petrov Egor. Created 09.04.17.
 */
public class MemoryUtilTest {

    @Rule
    public final TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("Starting test: " + description.getMethodName());
        }

    };

    @Rule
    public final Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            System.out.println(description.getMethodName() + " taken " + TimeUnit.NANOSECONDS.toMillis(nanos) + "ms");
            System.out.println("--------------------------------------------------------------------------\n\n");
        }
    };

    private void checkSize(Object o) {
        System.out.println("\nMeasuring size...");
        System.out.println("Class: " + o.getClass().getSimpleName() + " hash: " + System.identityHashCode(o));
        System.out.println("size " + MemoryUtil.sizeOf(o) + "bytes.\n");
    }

    @Test
    public void sizeOfObject() {
        checkSize(new Object());
    }

    @Test
    public void sizeOfEmptyStringSize() {
        checkSize("");
    }

    @Test
    public void sizeOfNotEmptyStringSize() {
        checkSize("ABCD");
    }

    @Test
    public void sizeOfEmptyArray() {
        checkSize(new Object[0]);
    }

    @Test
    public void sizeOfEmptyArrayOfPrimitives() {
        checkSize(new byte[0]);
    }

    @Test
    public void sizeOfEmptyArrayList() {
        checkSize(new ArrayList<>());
    }

    @Test
    public void measurementOfArrayListSizeGrowth() {
        List<Integer> list = new ArrayList<>();
        List<Double> checkPoints = new ArrayList<>();
        IntStream.rangeClosed(0, 10000).forEach(i -> {
            list.add(i + 127);
            checkPoints.add(1.0 * MemoryUtil.sizeOf(list) / list.size());
            if (i % 2500 == 0) {
                checkSize(list);
            }
        });
        System.out.println("Average size increase: " +
                Math.round(checkPoints.parallelStream().mapToDouble(d -> d).average().orElse(0)) +
                "bytes per element.\n");
    }

}
