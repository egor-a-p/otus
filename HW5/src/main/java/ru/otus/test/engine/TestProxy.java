package ru.otus.test.engine;

import lombok.extern.slf4j.Slf4j;
import ru.otus.test.api.After;
import ru.otus.test.api.Before;
import ru.otus.test.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;


/**
 * Created by egor on 30.07.17.
 */
@Slf4j
public class TestProxy<T> implements Iterable<Supplier<TestResult>>{

    public static <V> TestProxy<V> createTestProxy(Class<V> testClass) throws IllegalAccessException, InstantiationException {
        Objects.requireNonNull(testClass, "Test class is null!");
        Map<Class<? extends Annotation>, List<Method>> annotatedMethods = createMap();
        for (Method method : testClass.getMethods()) {
            checkAndAdd(annotatedMethods, method);
        }
        V instance = testClass.newInstance();
        return new TestProxy<>(instance, annotatedMethods);
    }

    private static void checkAndAdd(Map<Class<? extends Annotation>, List<Method>> annotatedMethods, Method method) {
        if (method.isAnnotationPresent(Before.class) && checkMethodArgs(method)) {
            annotatedMethods.get(Before.class).add(method);
        }
        if (method.isAnnotationPresent(Test.class) && checkMethodArgs(method)) {
            annotatedMethods.get(Test.class).add(method);
        }
        if (method.isAnnotationPresent(After.class) && checkMethodArgs(method)) {
            annotatedMethods.get(After.class).add(method);
        }
    }

    private static boolean checkMethodArgs(Method method) {
        if (method.getParameters().length != 0) {
            throw new UnsupportedOperationException("Method contains args!");
        }
        return true;
    }

    private static Map<Class<? extends Annotation>, List<Method>> createMap() {
        Map<Class<? extends Annotation>, List<Method>> map = new ConcurrentHashMap<>();
        map.put(Before.class, new ArrayList<>());
        map.put(Test.class, new ArrayList<>());
        map.put(After.class, new ArrayList<>());
        return map;
    }

    private final Map<Class<? extends Annotation>, List<Method>> annotatedMethods;
    private final T instance;



    @Override
    public Iterator<Supplier<TestResult>> iterator() {
        return new Iterator<Supplier<TestResult>>() {

            private Iterator<Method> testIterator = annotatedMethods.get(Test.class).iterator();

            @Override
            public boolean hasNext() {
                return testIterator.hasNext();
            }

            @Override
            public Supplier<TestResult> next() {
                return () -> {

                };
            }
        };
    }
}
