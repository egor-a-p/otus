package ru.otus.test.executor.stub;

import java.util.concurrent.atomic.AtomicInteger;

import ru.otus.test.api.After;
import ru.otus.test.api.Before;
import ru.otus.test.api.Test;

/**
 * Created by egor on 30.07.17.
 */
public class FullTestStub {

    public static final AtomicInteger beforeCounter = new AtomicInteger();
    public static final AtomicInteger afterCounter = new AtomicInteger();

    @Before
    public void before() {
        beforeCounter.incrementAndGet();
    }

    @Test
    public void passesTest() {

    }

    @Test
    public void failedTest() {
        throw new AssertionError();
    }

    @Test
    public void errorTest() {
        throw new RuntimeException();
    }

    @After
    public void after() {
        afterCounter.incrementAndGet();
    }
}
