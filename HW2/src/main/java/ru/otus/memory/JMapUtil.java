package ru.otus.memory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Petrov Egor. Created 09.04.17.
 */
public class JMapUtil {

    private JMapUtil() {
    }

    public static long sizeOf(Object object) {
        try {
            Objects.requireNonNull(object);
            String jvmName = ManagementFactory.getRuntimeMXBean().getName();
            int pid = Integer.parseInt(jvmName.split("@")[0]);
            Process process = Runtime.getRuntime().exec("jmap -histo " + pid);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String objectInfo = reader.lines().filter(
                    s -> s.endsWith(object.getClass().getCanonicalName())
            ).collect(Collectors.toList()).get(0);
            List<String> attributes = Arrays.stream(objectInfo.split(" ")).filter(
                    s -> !s.equals("")
            ).collect(Collectors.toList());
            long count = Long.parseLong(attributes.get(1));
            long size = Long.parseLong(attributes.get(2));
            return size / count;
        } catch (Exception e) {
            return 0;
        }
    }
}
