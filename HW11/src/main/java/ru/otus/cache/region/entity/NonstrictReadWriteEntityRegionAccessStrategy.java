package ru.otus.cache.region.entity;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

/**
 * @author e.petrov. Created 08 - 2017.
 */
public class NonstrictReadWriteEntityRegionAccessStrategy extends BaseEntityRegionAccessStrategy {
	public NonstrictReadWriteEntityRegionAccessStrategy(EntityRegionImpl entityRegion) {
		super(entityRegion);
	}

	@Override
	public SoftLock lockItem(SharedSessionContractImplementor session, Object key, Object version) throws CacheException {
		return null;
	}

	@Override
	public SoftLock lockRegion() throws CacheException {
		return null;
	}

	@Override
	public void unlockItem(SharedSessionContractImplementor session, Object key, SoftLock lock) throws CacheException {
		evict(key);
	}

	@Override
	public void remove(SharedSessionContractImplementor session, Object key) throws CacheException {
		evict(key);
	}

	@Override
	public boolean insert(SharedSessionContractImplementor session, Object key, Object value, Object version) throws CacheException {
		return false;
	}

	@Override
	public boolean afterInsert(SharedSessionContractImplementor session, Object key, Object value, Object version) throws CacheException {
		return false;
	}

	@Override
	public boolean update(SharedSessionContractImplementor session, Object key, Object value, Object currentVersion, Object previousVersion)
		throws CacheException {
		evict(key);
		return false;
	}

	@Override
	public boolean afterUpdate(SharedSessionContractImplementor session, Object key, Object value, Object currentVersion, Object previousVersion,
	                           SoftLock lock) throws CacheException {
		evict(key);
		return false;
	}
}
