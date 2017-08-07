package ru.otus.cache.region.collection;

import java.util.Comparator;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.persister.collection.CollectionPersister;

import ru.otus.cache.region.AbstractReadWriteAccessStrategy;

/**
 * @author e.petrov. Created 08 - 2017.
 */
public class ReadWriteCollectionRegionAccessStrategy extends AbstractReadWriteAccessStrategy implements CollectionRegionAccessStrategy {

	private final CollectionRegionImpl collectionRegion;

	public ReadWriteCollectionRegionAccessStrategy(CollectionRegionImpl collectionRegion) {
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

	@Override
	public SoftLock lockRegion() throws CacheException {
		return null;
	}

	@Override
	public void remove(SharedSessionContractImplementor session, Object key) throws CacheException {
	}

	@Override
	protected Comparator getVersionComparator() {
		return collectionRegion.getCacheDataDescription().getVersionComparator();
	}
}
