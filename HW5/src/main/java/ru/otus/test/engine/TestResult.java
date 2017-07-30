package ru.otus.test.engine;

import lombok.Value;

/**
 * Created by egor on 30.07.17.
 */
@Value
public class TestResult {
    private final boolean passed;
    private final AssertionError error;
}
