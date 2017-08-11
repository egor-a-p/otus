package ru.otus.gc.statistics;

import com.sun.management.GarbageCollectionNotificationInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by egor on 31.07.17.
 */
public class GCStatisticsAccumulator {

    private final ReentrantLock lock;
    private final Map<GCType, List<Long>> gc;

    GCStatisticsAccumulator() {
        lock = new ReentrantLock();
        gc = new HashMap<>();
        gc.put(GCType.MINOR, new ArrayList<>());
        gc.put(GCType.MAJOR, new ArrayList<>());
    }

    public void accumulate(GarbageCollectionNotificationInfo info) {
        lock.lock();
        try {
            if(info.getGcAction().contains(GCType.MINOR.name().toLowerCase())) {
                gc.get(GCType.MINOR).add(info.getGcInfo().getDuration());
            } else {
                gc.get(GCType.MAJOR).add(info.getGcInfo().getDuration());
            }
        } finally {
            lock.unlock();
        }
    }

    public GCStatistics release() {
        lock.lock();
        try {
            return GCStatistics.builder()
                    .majorCount(gc.get(GCType.MAJOR).size())
                    .minorCount(gc.get(GCType.MINOR).size())
                    .majorDuration(gc.get(GCType.MAJOR).stream().mapToLong(l -> l).sum())
                    .minorDuration(gc.get(GCType.MINOR).stream().mapToLong(l -> l).sum())
                    .build();
        } finally {
            gc.get(GCType.MINOR).clear();
            gc.get(GCType.MAJOR).clear();
            lock.unlock();
        }
    }

    public enum GCType {
        MINOR,
        MAJOR
    }
}
