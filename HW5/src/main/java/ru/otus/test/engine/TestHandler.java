package ru.otus.test.engine;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by egor on 30.07.17.
 */
@Slf4j
public class TestHandler implements InvocationHandler {

    private final List<Method> before;
    private final List<Method> after;

    TestHandler(List<Method> before, List<Method> after) {
        this.before = before;
        this.after = after;
    }

    @Override
    public TestResult invoke(Object testClassInstance, Method method, Object[] args) {
        TestResult.TestResultBuilder builder = TestResult.builder()
            .testClassName(testClassInstance.getClass().getName())
            .testMethodName(method.getName());
        try {
            before(testClassInstance);
            method.invoke(testClassInstance);
            after(testClassInstance);

            return builder.status(TestResult.Status.PASSED).throwable(null).build();
        } catch (InvocationTargetException e) {
	        if (e.getCause() != null && e.getCause().getClass() == AssertionError.class) {
		        after(testClassInstance);
		        return builder.status(TestResult.Status.FAILED).throwable(e.getCause()).build();
	        } else {
		        return builder.status(TestResult.Status.ERROR).throwable(e.getCause()).build();
	        }
        } catch (Throwable throwable) {
            return builder.status(TestResult.Status.ERROR).throwable(throwable).build();
        }
    }

    private void after(Object testClassInstance) {
        after.forEach(m -> {
            try {
                m.invoke(testClassInstance);
            } catch (Exception e) {
                throw new RuntimeException("After exception:", e);
            }
        });
    }

    private void before(Object testClassInstance) {
        before.forEach(m -> {
            try {
                m.invoke(testClassInstance);
            } catch (Exception e) {
                throw new RuntimeException("Before exception:", e);
            }
        });
    }
}
