package ru.otus.cache.region.id;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

/**
 * @author e.petrov. Created 08 - 2017.
 */
public class ReadOnlyNaturalIdRegionAccessStrategy extends BaseNaturalIdRegionAccessStrategy {
	public ReadOnlyNaturalIdRegionAccessStrategy(NaturalIdRegionImpl naturalIdRegion) {
		super(naturalIdRegion);
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
}
