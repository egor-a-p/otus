package ru.otus.cache.region.entity;

import java.util.Comparator;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.persister.entity.EntityPersister;

import ru.otus.cache.region.AbstractReadWriteAccessStrategy;
import ru.otus.cache.region.Item;
import ru.otus.cache.region.LockInfo;
import ru.otus.cache.region.Lockable;

/**
 * @author e.petrov. Created 08 - 2017.
 */
public class ReadWriteEntityRegionAccessStrategy extends AbstractReadWriteAccessStrategy implements EntityRegionAccessStrategy {
	private final EntityRegionImpl entityRegion;

	public ReadWriteEntityRegionAccessStrategy(EntityRegionImpl entityRegion) {
		this.entityRegion = entityRegion;
	}

	@Override
	public Object generateCacheKey(Object id, EntityPersister persister, SessionFactoryImplementor factory, String tenantIdentifier) {
		return entityRegion.getKeysFactory().createEntityKey( id, persister, factory, tenantIdentifier );
	}

	@Override
	public Object getCacheKeyId(Object cacheKey) {
		return entityRegion.getKeysFactory().getEntityId(cacheKey);
	}

	@Override
	public EntityRegionImpl getRegion() {
		return null;
	}

	@Override
	public boolean insert(SharedSessionContractImplementor session, Object key, Object value, Object version) throws CacheException {
		return false;
	}

	@Override
	public boolean afterInsert(SharedSessionContractImplementor session, Object key, Object value, Object version) throws CacheException {
		try {
			writeLock.lock();
			Lockable item = (Lockable) entityRegion.get(session, key);
			if (item == null) {
				entityRegion.put(session, key, new Item(value, version, entityRegion.nextTimestamp()));
				return true;
			} else {
				return false;
			}
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public boolean update(SharedSessionContractImplementor session, Object key, Object value, Object currentVersion, Object previousVersion)
		throws CacheException {
		return false;
	}

	@Override
	public boolean afterUpdate(SharedSessionContractImplementor session, Object key, Object value, Object currentVersion, Object previousVersion,
	                           SoftLock lock) throws CacheException {
		try {
			writeLock.lock();
			Lockable item = (Lockable) entityRegion.get(session, key);

			if (item != null && item.isUnlockable(lock)) {
				LockInfo lockItem = (LockInfo) item;
				if (lockItem.wasLockedConcurrently()) {
					decrementLock(session, key, lockItem);
					return false;
				} else {
					entityRegion.put(session, key, new Item(value, currentVersion, entityRegion.nextTimestamp()));
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
	protected boolean isDefaultMinimalPutOverride() {
		return entityRegion.getSettings().isMinimalPutsEnabled();
	}

	@Override
	protected Comparator getVersionComparator() {
		return entityRegion.getCacheDataDescription().getVersionComparator();
	}

	@Override
	public SoftLock lockRegion() throws CacheException {
		return null;
	}

	@Override
	public void remove(SharedSessionContractImplementor session, Object key) throws CacheException {
	}
}
