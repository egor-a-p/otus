package ru.otus.cache.region.entity;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.persister.entity.EntityPersister;

import ru.otus.cache.region.AbstractRegionAccessStrategy;

/**
 * @author e.petrov. Created 08 - 2017.
 */
abstract class BaseEntityRegionAccessStrategy extends AbstractRegionAccessStrategy implements EntityRegionAccessStrategy {

	private final EntityRegionImpl entityRegion;

	BaseEntityRegionAccessStrategy(EntityRegionImpl entityRegion) {
		this.entityRegion = entityRegion;
	}

	@Override
	public Object generateCacheKey(Object id, EntityPersister persister, SessionFactoryImplementor factory, String tenantIdentifier) {
		return entityRegion.getKeysFactory().createEntityKey(id, persister, factory, tenantIdentifier);
	}

	@Override
	public Object getCacheKeyId(Object cacheKey) {
		return entityRegion.getKeysFactory().getCollectionId(cacheKey);
	}

	@Override
	public EntityRegionImpl getRegion() {
		return entityRegion;
	}

	@Override
	protected boolean isDefaultMinimalPutOverride() {
		return entityRegion.getSettings().isMinimalPutsEnabled();
	}

	@Override
	public boolean insert(SharedSessionContractImplementor session, Object key, Object value, Object version) throws CacheException {
		return putFromLoad(session, key, value, 0, version);
	}

	@Override
	public boolean afterInsert(SharedSessionContractImplementor session, Object key, Object value, Object version) throws CacheException {
		return true;
	}

	@Override
	public boolean update(SharedSessionContractImplementor session, Object key, Object value, Object currentVersion, Object previousVersion)
		throws CacheException {
		return false;
	}

	@Override
	public boolean afterUpdate(SharedSessionContractImplementor session, Object key, Object value, Object currentVersion, Object previousVersion,
	                           SoftLock lock) throws CacheException {
		return false;
	}
}
