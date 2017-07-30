package ru.otus.test.engine;

import lombok.extern.slf4j.Slf4j;
import ru.otus.test.api.After;
import ru.otus.test.api.Before;

import java.lang.AssertionError;
import java.lang.Boolean;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.Throwable;
import java.lang.UnsupportedOperationException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by egor on 30.07.17.
 */
@Slf4j
public class TestHandler implements InvocationHandler {

    private final List<Method> before;
    private final List<Method> after;

    public TestHandler(List<Method> before, List<Method> after) {
        this.before = before;
        this.after = after;
    }

    @Override
    public Boolean invoke(Object testClassInstance, Method method, Object[] args) {
        try {
            Objects.requireNonNull(testClassInstance, "Test class instance is null!");
            Objects.requireNonNull(method, "Test class method is null!");
            if (method.getParameters().length != 0) {
                throw new UnsupportedOperationException("Test method has parameters!");
            }

            before(testClassInstance);
            method.invoke(testClassInstance);
            after(testClassInstance);

            return true;
        } catch (AssertionError assertionError) {
            log.warn("Test {} failed: {}!", method.getName(), assertionError.getMessage());
            return false;
        } catch (Throwable throwable) {
            log.error("Test failed: ", throwable);
            return false;
        }
    }

    private void after(Object testClassInstance) {
        after.forEach(m -> {
            try {
                if (m.getParameters().length != 0) {
                    throw new UnsupportedOperationException("After method has parameters!");
                }
                m.invoke(testClassInstance);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void before(Object testClassInstance) {
        before.forEach(m -> {
            try {
                if (m.getParameters().length != 0) {
                    throw new UnsupportedOperationException("Before method has parameters!");
                }
                m.invoke(testClassInstance);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
