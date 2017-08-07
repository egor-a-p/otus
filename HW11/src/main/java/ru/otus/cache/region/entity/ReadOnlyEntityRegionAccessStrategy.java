package ru.otus.cache.region.entity;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

/**
 * @author e.petrov. Created 08 - 2017.
 */
public class ReadOnlyEntityRegionAccessStrategy extends BaseEntityRegionAccessStrategy {
	public ReadOnlyEntityRegionAccessStrategy(EntityRegionImpl entityRegion) {
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
	}

	@Override
	public boolean insert(SharedSessionContractImplementor session, Object key, Object value, Object version) throws CacheException {
		return false;
	}

	@Override
	public boolean afterInsert(SharedSessionContractImplementor session, Object key, Object value, Object version) throws CacheException {
		getRegion().put(session, key, value);
		return true;
	}

	@Override
	public boolean update(SharedSessionContractImplementor session, Object key, Object value, Object currentVersion, Object previousVersion)
		throws CacheException {
		throw new UnsupportedOperationException( "Can't write to a readonly object" );
	}

	@Override
	public boolean afterUpdate(SharedSessionContractImplementor session, Object key, Object value, Object currentVersion, Object previousVersion,
	                           SoftLock lock) throws CacheException {
		throw new UnsupportedOperationException( "Can't write to a readonly object" );
	}
}
