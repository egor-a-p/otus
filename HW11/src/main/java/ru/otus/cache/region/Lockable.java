package ru.otus.cache.region;

import java.util.Comparator;
import java.util.UUID;

import org.hibernate.cache.spi.access.SoftLock;

/**
 * @author e.petrov. Created 08 - 2017.
 */
public interface Lockable {
	boolean isReadable(long txTimestamp);

	boolean isWritable(long txTimestamp, Object version, Comparator versionComparator);

	Object getValue();

	boolean isUnlockable(SoftLock lock);

	LockInfo lock(long timeout, UUID uuid, long lockId);
}
