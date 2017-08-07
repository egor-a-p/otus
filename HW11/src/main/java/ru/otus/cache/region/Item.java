package ru.otus.cache.region;

import java.io.Serializable;
import java.util.Comparator;
import java.util.UUID;

import org.hibernate.cache.spi.access.SoftLock;

public final class Item implements Serializable, Lockable {
	private static final long serialVersionUID = 1L;
	private final Object value;
	private final Object version;
	private final long timestamp;

	public Item(Object value, Object version, long timestamp) {
		this.value = value;
		this.version = version;
		this.timestamp = timestamp;
	}

	@Override
	public boolean isReadable(long txTimestamp) {
		return txTimestamp > timestamp;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean isWritable(long txTimestamp, Object newVersion, Comparator versionComparator) {
		return version != null && versionComparator.compare(version, newVersion) < 0;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public boolean isUnlockable(SoftLock lock) {
		return false;
	}

	@Override
	public LockInfo lock(long timeout, UUID uuid, long lockId) {
		return new LockInfo(timeout, uuid, lockId, version);
	}
}
