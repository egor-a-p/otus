package ru.otus.test.executor.stub;

import ru.otus.test.api.Test;

/**
 * Created by egor on 30.07.17.
 */
public class SimpleTestStub {

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

}
