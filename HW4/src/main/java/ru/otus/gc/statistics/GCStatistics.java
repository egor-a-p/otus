package ru.otus.gc.statistics;


import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

@Value
@Builder
public class GCStatistics {
    private final int majorCount;
    private final long majorDuration;
    private final int minorCount;
    private final long minorDuration;

    @Override
    public String toString() {
        return new StringBuilder("\n")
                .append("Major GC:")
                .append("\n\t")
                .append(StringUtils.right("count - " + majorCount, 30))
                .append("\n\t")
                .append(StringUtils.right("total duration - " + majorDuration, 30))
                .append("ms\n\t")
                .append(StringUtils.right("average duration - " + (majorCount == 0 ? 0 : majorDuration / majorCount), 30))
                .append("ms\n")
                .append("Minor GC:")
                .append("\n\t")
                .append(StringUtils.right("count - " + minorCount, 30))
                .append("\n\t")
                .append(StringUtils.right("total duration - " + minorDuration, 30))
                .append("ms\n\t")
                .append(StringUtils.right("average duration - " + (minorCount == 0 ? 0 : minorDuration / minorCount), 30))
                .append("ms\n")
                .toString();
    }
}
