package ru.otus.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import lombok.extern.slf4j.Slf4j;
import ru.otus.test.engine.TestProxy;
import ru.otus.test.engine.TestResult;


@Slf4j
public class TestExecutor {

    public static List<TestResult> execute(Class... classes) {
        List<TestResult> resultList = new ArrayList<>();
        Arrays.stream(classes).map(c -> {
            try {
                return TestProxy.createTestProxy(c);
            } catch (Exception e) {
                log.error("Can't create test proxy: ", e);
                return TestProxy.createNullProxy();
            }
        }).map(TestProxy::test).forEach(l -> l.forEach(r -> {
            resultList.add(r);
            switch (r.getStatus()) {
                case PASSED:
                    log.debug("Test {} passed!", r.getTestName());
                    break;
                case FAILED:
                    log.warn("Test " + r.getTestName() + " failed!", r.getThrowable());
                    break;
                case ERROR:
                    log.error("Error in test:  " + r.getTestName(), r.getThrowable());
            }
        }));
        return Collections.unmodifiableList(resultList);
    }

    public static List<TestResult> execute(String packageName) {
	    Reflections reflections = new Reflections(new ConfigurationBuilder()
		                                              .setScanners(new SubTypesScanner(false), new ResourcesScanner())
		                                              .setUrls(ClasspathHelper.forClassLoader(ClasspathHelper.contextClassLoader()))
		                                              .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName))));
	    return execute(reflections.getSubTypesOf(Object.class).stream().toArray(Class[]::new));
    }
}
