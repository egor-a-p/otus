package ru.otus.cache.region;

import java.io.Serializable;
import java.util.Comparator;
import java.util.UUID;

import org.hibernate.cache.spi.access.SoftLock;


public final class LockInfo implements Serializable, Lockable, SoftLock {
	private static final long serialVersionUID = 2L;
	private final UUID sourceUuid;
	private final long lockId;
	private final Object version;
	private long timeout;
	private int multiplicity = 1;
	private long unlockTimestamp;
	private boolean concurrent;

	LockInfo(long timeout, UUID sourceUuid, long lockId, Object version) {
		this.timeout = timeout;
		this.lockId = lockId;
		this.version = version;
		this.sourceUuid = sourceUuid;
	}

	@Override
	public boolean isReadable(long txTimestamp) {
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean isWritable(long txTimestamp, Object newVersion, Comparator versionComparator) {
		if (txTimestamp > timeout) {
			return true;
		}
		return multiplicity <= 0 && (version == null ? txTimestamp > unlockTimestamp : versionComparator.compare(version, newVersion) < 0);
	}

	@Override
	public Object getValue() {
		return null;
	}

	@Override
	public boolean isUnlockable(SoftLock lock) {
		return equals(lock);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o instanceof LockInfo) {
			return (lockId == ((LockInfo) o).lockId) && sourceUuid.equals(((LockInfo) o).sourceUuid);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int hash = (sourceUuid != null ? sourceUuid.hashCode() : 0);
		int temp = (int) lockId;
		for (int i = 1; i < Long.SIZE / Integer.SIZE; i++) {
			temp ^= (lockId >>> (i * Integer.SIZE));
		}
		return hash + temp;
	}

	public boolean wasLockedConcurrently() {
		return concurrent;
	}

	@Override
	public LockInfo lock(long timeout, UUID uuid, long lockId) {
		concurrent = true;
		multiplicity++;
		this.timeout = timeout;
		return this;
	}

	public void unlock(long timestamp) {
		if (--multiplicity == 0) {
			unlockTimestamp = timestamp;
		}
	}

	@Override
	public String toString() {
		return "Lock Source-UUID:" + sourceUuid + " Lock-ID:" + lockId;
	}
}
