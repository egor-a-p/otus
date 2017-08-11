package ru.otus.gc.statistics;

import javax.management.NotificationEmitter;
import javax.management.openmbean.CompositeData;
import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicReference;

import static com.sun.management.GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION;
import static com.sun.management.GarbageCollectionNotificationInfo.from;

public class GCUtil {

    private static final AtomicReference<GCStatisticsAccumulator> statisticsReference = new AtomicReference<>(null);

    public static GCStatisticsAccumulator init() {
        if (statisticsReference.compareAndSet(null, new GCStatisticsAccumulator())) {
            ManagementFactory.getGarbageCollectorMXBeans().forEach(gcBean -> (
                    (NotificationEmitter) gcBean).addNotificationListener(
                    (n, h) -> ((GCStatisticsAccumulator) h).accumulate(from((CompositeData) n.getUserData())),
                    n -> GARBAGE_COLLECTION_NOTIFICATION.equals(n.getType()),
                    statisticsReference.get()));
        }
        return statisticsReference.get();
    }


    private GCUtil() {
    }
}
