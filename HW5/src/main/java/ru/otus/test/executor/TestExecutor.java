package ru.otus.test.executor;

import lombok.extern.slf4j.Slf4j;
import ru.otus.test.api.Test;
import ru.otus.test.engine.TestHandler;

import java.util.Arrays;
import java.util.Objects;


/**
 * Created by egor on 30.07.17.
 */
@Slf4j
public class TestExecutor {

    public static void execute(Class... classes) {
        Arrays.stream(classes).forEach(c -> {
            try {
                Objects.requireNonNull(c, "Can't execute null class test!");
                TestHandler handler = new TestHandler(c);
                Object testObject = c.newInstance();
                Arrays.stream(c.getMethods())
                        .filter(m -> m.isAnnotationPresent(Test.class))
                        .forEach(m -> {
                            if (handler.invoke(testObject, m, new Object[]{})) {
                                log.debug("Test {}#{} passed!", c.getName(), m.getName());
                            }
                        });
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("Can't create instance of {}!", c.getName());
            }
        });
    }

    public static void execute(String packageName) {

    }
}
