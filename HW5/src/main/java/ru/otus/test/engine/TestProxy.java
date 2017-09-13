package ru.otus.test.engine;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import ru.otus.test.api.After;
import ru.otus.test.api.Before;
import ru.otus.test.api.Test;


public class TestProxy {

    public static TestProxy createTestProxy(Class testClass) throws IllegalAccessException, InstantiationException {
        Objects.requireNonNull(testClass, "Test class is null!");
        Map<Class<? extends Annotation>, List<Method>> annotatedMethods = createMap();
        for (Method method : testClass.getMethods()) {
            checkAndAdd(annotatedMethods, method);
        }
        Object instance = testClass.newInstance();
        return new TestProxy(annotatedMethods, instance);
    }

    public static TestProxy createNullProxy() {
	    return new NullProxy();
    }

    private static class NullProxy extends TestProxy {
	    private NullProxy() {
		    super(Collections.emptyMap(), null);
	    }

	    @Override
	    public List<TestResult> test() {
		    return Collections.emptyList();
	    }
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
	    Objects.requireNonNull(method, "Test class method is null!");
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
    private final Object instance;

    private TestProxy(Map<Class<? extends Annotation>, List<Method>> annotatedMethods, Object instance) {
        this.annotatedMethods = Collections.unmodifiableMap(annotatedMethods);
        this.instance = instance;
    }

	public List<TestResult> test() {
		TestHandler handler = new TestHandler(annotatedMethods.get(Before.class), annotatedMethods.get(After.class));
		return annotatedMethods.get(Test.class)
			.stream()
			.map(m -> handler.invoke(instance, m, null))
			.collect(Collectors.toList());
	}
}
