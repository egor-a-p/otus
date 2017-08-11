package ru.otus.gc;

import lombok.extern.slf4j.Slf4j;
import ru.otus.gc.statistics.GCStatisticsAccumulator;
import ru.otus.gc.statistics.GCUtil;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @author e.petrov. Created 07 - 2017.
 */
@Slf4j
public class Main {
    //-Xmx512M -Xms512M
    public static void main(String[] args) throws InterruptedException {
        Thread leakyThread = new LeakyThread();
        GCStatisticsAccumulator accumulator = GCUtil.init();
        new Timer(true).scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        log.info(accumulator.release().toString());
                    }
                },
                TimeUnit.MINUTES.toMillis(1),
                TimeUnit.MINUTES.toMillis(1));
        leakyThread.start();
        leakyThread.join();
    }
}
