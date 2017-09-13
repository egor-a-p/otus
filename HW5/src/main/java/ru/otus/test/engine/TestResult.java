package ru.otus.test.engine;

import lombok.Builder;
import lombok.Value;

/**
 * Created by egor on 30.07.17.
 */
@Value
@Builder
public class TestResult {
    private final Status status;
    private final Throwable throwable;
    private final String testClassName;
    private final String testMethodName;

    public String getTestName() {
        return testClassName + "#" + testMethodName;
    }

    public enum Status {
        PASSED,
        FAILED,
        ERROR
    }
}
