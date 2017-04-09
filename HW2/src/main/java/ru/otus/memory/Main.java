package ru.otus.memory;

import java.util.stream.IntStream;

/**
 * @author Petrov Egor. Created 08.04.17.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        StringBuilder stringBuilder = new StringBuilder();
        IntStream.range(0, 1000).forEach(i -> {
            stringBuilder.append(i);
            System.out.println(MemoryUtil.sizeOf(stringBuilder.toString()));
        });
    }

}
