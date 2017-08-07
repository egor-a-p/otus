package ru.otus.cache.region.collection;

import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.persister.collection.CollectionPersister;

import ru.otus.cache.region.AbstractRegionAccessStrategy;

/**
 * @author e.petrov. Created 08 - 2017.
 */
abstract class BaseCollectionRegionAccessStrategy extends AbstractRegionAccessStrategy implements CollectionRegionAccessStrategy {

	private final CollectionRegionImpl collectionRegion;

	BaseCollectionRegionAccessStrategy(CollectionRegionImpl collectionRegion) {
		this.collectionRegion = collectionRegion;
	}

	@Override
	public Object generateCacheKey(Object id, CollectionPersister persister, SessionFactoryImplementor factory, String tenantIdentifier) {
		return collectionRegion.getKeysFactory().createCollectionKey(id, persister, factory, tenantIdentifier);
	}

	@Override
	public Object getCacheKeyId(Object cacheKey) {
		return collectionRegion.getKeysFactory().getCollectionId(cacheKey);
	}

	@Override
	public CollectionRegionImpl getRegion() {
		return collectionRegion;
	}

	@Override
	protected boolean isDefaultMinimalPutOverride() {
		return collectionRegion.getSettings().isMinimalPutsEnabled();
	}
}
