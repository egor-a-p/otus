package ru.otus.cache.region;

import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractReadWriteAccessStrategy extends AbstractRegionAccessStrategy {
	private final UUID uuid = UUID.randomUUID();
	private final AtomicLong nextLockId = new AtomicLong();
	private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
	protected java.util.concurrent.locks.Lock readLock = reentrantReadWriteLock.readLock();
	protected java.util.concurrent.locks.Lock writeLock = reentrantReadWriteLock.writeLock();

	@Override
	public final Object get(SharedSessionContractImplementor session, Object key, long txTimestamp) throws CacheException {
		try {
			readLock.lock();
			Lockable item = (Lockable) getRegion().get(session, key);

			boolean readable = item != null && item.isReadable(txTimestamp);
			if (readable) {
				return item.getValue();
			} else {
				return null;
			}
		} finally {
			readLock.unlock();
		}
	}

	protected abstract Comparator getVersionComparator();

	@Override
	public final boolean putFromLoad(SharedSessionContractImplementor session, Object key, Object value, long txTimestamp, Object version,
	                                 boolean minimalPutOverride) throws CacheException {
		try {
			writeLock.lock();
			Lockable item = (Lockable) getRegion().get(session, key);
			boolean writable = item == null || item.isWritable(txTimestamp, version, getVersionComparator());
			if (writable) {
				getRegion().put(session, key, new Item(value, version, getRegion().nextTimestamp()));
				return true;
			} else {
				return false;
			}
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public final SoftLock lockItem(SharedSessionContractImplementor session, Object key, Object version) throws CacheException {
		try {
			writeLock.lock();
			Lockable item = (Lockable) getRegion().get(session, key);
			long timeout = getRegion().nextTimestamp() + getRegion().getTimeout();
			final LockInfo lockInfo = (item == null) ? new LockInfo(timeout, uuid, nextLockId(), version) : item.lock(timeout, uuid, nextLockId());
			getRegion().put(session, key, lockInfo);
			return lockInfo;
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public final void unlockItem(SharedSessionContractImplementor session, Object key, SoftLock lock) throws CacheException {
		try {
			writeLock.lock();
			Lockable item = (Lockable) getRegion().get(session, key);
			if ((item != null) && item.isUnlockable(lock)) {
				decrementLock(session, key, (LockInfo) item);
			} else {
				handleLockExpiry(session, key, item);
			}
		} finally {
			writeLock.unlock();
		}
	}

	private long nextLockId() {
		return nextLockId.getAndIncrement();
	}

	protected void decrementLock(SharedSessionContractImplementor session, Object key, LockInfo lockInfo) {
		lockInfo.unlock(getRegion().nextTimestamp());
		getRegion().put(session, key, lockInfo);
	}

	protected void handleLockExpiry(SharedSessionContractImplementor session, Object key, Lockable lock) {
		long ts = getRegion().nextTimestamp() + getRegion().getTimeout();
		LockInfo newLockInfo = new LockInfo(ts, uuid, nextLockId.getAndIncrement(), null);
		newLockInfo.unlock(ts);
		getRegion().put(session, key, newLockInfo);
	}
}
