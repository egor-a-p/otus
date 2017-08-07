package ru.otus.cache.region.id;

import java.util.Comparator;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.persister.entity.EntityPersister;

import ru.otus.cache.region.AbstractReadWriteAccessStrategy;
import ru.otus.cache.region.Item;
import ru.otus.cache.region.LockInfo;
import ru.otus.cache.region.Lockable;

/**
 * @author e.petrov. Created 08 - 2017.
 */
public class ReadWriteNaturalIdRegionAccessStrategy extends AbstractReadWriteAccessStrategy implements NaturalIdRegionAccessStrategy {
	private final NaturalIdRegionImpl naturalIdRegion;

	public ReadWriteNaturalIdRegionAccessStrategy(NaturalIdRegionImpl naturalIdRegion) {
		this.naturalIdRegion = naturalIdRegion;
	}

	@Override
	public Object generateCacheKey(Object[] naturalIdValues, EntityPersister persister, SharedSessionContractImplementor session) {
		return naturalIdRegion.getKeysFactory().createNaturalIdKey(naturalIdValues, persister, session);
	}

	@Override
	public Object[] getNaturalIdValues(Object cacheKey) {
		return naturalIdRegion.getKeysFactory().getNaturalIdValues(cacheKey);
	}

	@Override
	public NaturalIdRegionImpl getRegion() {
		return naturalIdRegion;
	}

	@Override
	protected boolean isDefaultMinimalPutOverride() {
		return naturalIdRegion.getSettings().isMinimalPutsEnabled();
	}

	@Override
	public boolean insert(SharedSessionContractImplementor session, Object key, Object value) throws CacheException {
		return false;
	}

	@Override
	public boolean afterInsert(SharedSessionContractImplementor session, Object key, Object value) throws CacheException {
		try {
			writeLock.lock();
			Lockable item = (Lockable) naturalIdRegion.get(session, key);
			if (item == null) {
				naturalIdRegion.put(session, key, new Item(value, null, naturalIdRegion.nextTimestamp()));
				return true;
			} else {
				return false;
			}
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public boolean update(SharedSessionContractImplementor session, Object key, Object value) throws CacheException {
		return false;
	}

	@Override
	public boolean afterUpdate(SharedSessionContractImplementor session, Object key, Object value, SoftLock lock) throws CacheException {
		try {
			writeLock.lock();
			Lockable item = (Lockable) naturalIdRegion.get(session, key);

			if (item != null && item.isUnlockable(lock)) {
				LockInfo lockItem = (LockInfo) item;
				if (lockItem.wasLockedConcurrently()) {
					decrementLock(session, key, lockItem);
					return false;
				} else {
					naturalIdRegion.put(session, key, new Item(value, null, naturalIdRegion.nextTimestamp()));
					return true;
				}
			} else {
				handleLockExpiry(session, key, item);
				return false;
			}
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	protected Comparator getVersionComparator() {
		return naturalIdRegion.getCacheDataDescription().getVersionComparator();
	}

	@Override
	public SoftLock lockRegion() throws CacheException {
		return null;
	}

	@Override
	public void remove(SharedSessionContractImplementor session, Object key) throws CacheException {
	}
}
