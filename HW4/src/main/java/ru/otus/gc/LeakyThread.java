package ru.otus.gc;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;


/**
 * @author e.petrov. Created 07 - 2017.
 */
@Slf4j
public class LeakyThread extends Thread {

    private List<byte[]> list = new ArrayList<>();

    @Override
	public void run() {
        //много магических чисел
        IntStream.range(0, Integer.MAX_VALUE).forEach(i -> {
            list.add(new byte[2 * i + 1024]);
            if (i != 0 && i % 300 == 0) {
                try {
                    list.removeAll(list.subList(0, list.size() / 2));
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    log.error("Error:", e);
                }
            }
        });
	}
}
