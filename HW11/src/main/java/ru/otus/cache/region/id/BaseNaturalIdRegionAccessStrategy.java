package ru.otus.cache.region.id;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.persister.entity.EntityPersister;

import ru.otus.cache.region.AbstractRegionAccessStrategy;

/**
 * @author e.petrov. Created 08 - 2017.
 */
abstract class BaseNaturalIdRegionAccessStrategy extends AbstractRegionAccessStrategy implements NaturalIdRegionAccessStrategy {
	private final NaturalIdRegionImpl naturalIdRegion;

	BaseNaturalIdRegionAccessStrategy(NaturalIdRegionImpl naturalIdRegion) {
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
		return putFromLoad(session, key, value, 0, null);
	}

	@Override
	public boolean afterInsert(SharedSessionContractImplementor session, Object key, Object value) throws CacheException {
		return false;
	}

	@Override
	public boolean update(SharedSessionContractImplementor session, Object key, Object value) throws CacheException {
		return putFromLoad(session, key, value, 0, null);
	}

	@Override
	public boolean afterUpdate(SharedSessionContractImplementor session, Object key, Object value, SoftLock lock) throws CacheException {
		return false;
	}
}
