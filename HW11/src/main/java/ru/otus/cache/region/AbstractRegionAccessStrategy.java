package ru.otus.cache.region;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.access.RegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author e.petrov. Created 08 - 2017.
 */
@Slf4j
public abstract class AbstractRegionAccessStrategy implements RegionAccessStrategy {

	protected abstract AbstractRegion getRegion();

	protected abstract boolean isDefaultMinimalPutOverride();

	@Override
	public Object get(SharedSessionContractImplementor session, Object key, long txTimestamp) throws CacheException {
		return getRegion().get(session, key);
	}

	@Override
	public boolean putFromLoad(SharedSessionContractImplementor session, Object key, Object value, long txTimestamp, Object version)
		throws CacheException {
		return putFromLoad(session, key, value, txTimestamp, version, isDefaultMinimalPutOverride());
	}

	@Override
	public boolean putFromLoad(SharedSessionContractImplementor session, Object key, Object value, long txTimestamp, Object version,
	                           boolean minimalPutOverride) throws CacheException {
		if (key == null || value == null) {
			return false;
		}
		if (minimalPutOverride && getRegion().contains(key)) {
			log.debug("Item already cached: {}", key);
			return false;
		}
		log.debug("Caching: {}", key);
		getRegion().put(session, key, value);
		return true;
	}

	@Override
	public void unlockRegion(SoftLock lock) throws CacheException {
		evictAll();
	}

	@Override
	public void removeAll() throws CacheException {
		evictAll();
	}

	@Override
	public void evict(Object key) throws CacheException {
		getRegion().evict(key);
	}

	@Override
	public void evictAll() throws CacheException {
		getRegion().evictAll();
	}
}
