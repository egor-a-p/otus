package ru.otus.test.engine;

import ru.otus.test.api.After;
import ru.otus.test.api.Before;
import ru.otus.test.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class TestProxy {

    public static TestProxy createTestProxy(Class testClass) throws IllegalAccessException, InstantiationException {
        Objects.requireNonNull(testClass, "Test class is null!");
        List<Method> beforeMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();
        for (Method method : testClass.getMethods()) {
            if (method.isAnnotationPresent(Before.class) && checkMethodArgs(method)) {
                beforeMethods.add(method);
            }
            if (method.isAnnotationPresent(Test.class) && checkMethodArgs(method)) {
                testMethods.add(method);
            }
            if (method.isAnnotationPresent(After.class) && checkMethodArgs(method)) {
                afterMethods.add(method);
            }
        }
        Object instance = testClass.newInstance();
        return new TestProxy(beforeMethods, testMethods, afterMethods, instance);
    }

    public static TestProxy createNullProxy() {
	    return new NullProxy();
    }

    private static class NullProxy extends TestProxy {
	    private NullProxy() {
		    super(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), null);
	    }

	    @Override
	    public List<TestResult> test() {
		    return Collections.emptyList();
	    }
    }

    private static boolean checkMethodArgs(Method method) {
	    Objects.requireNonNull(method, "Test class method is null!");
        if (method.getParameters().length != 0) {
            throw new UnsupportedOperationException("Method contains args!");
        }
        return true;
    }

    private final List<Method> beforeMethods;
    private final List<Method> testMethods;
    private final List<Method> afterMethods;
    private final Object instance;

    public TestProxy(List<Method> beforeMethods, List<Method> testMethods, List<Method> afterMethods, Object instance) {
        this.beforeMethods = Collections.unmodifiableList(beforeMethods);
        this.testMethods = Collections.unmodifiableList(testMethods);
        this.afterMethods = Collections.unmodifiableList(afterMethods);
        this.instance = instance;
    }

	public List<TestResult> test() {
		TestHandler handler = new TestHandler(beforeMethods, afterMethods);
		return testMethods
			.stream()
			.map(m -> handler.invoke(instance, m, null))
			.collect(Collectors.toList());
	}
}
