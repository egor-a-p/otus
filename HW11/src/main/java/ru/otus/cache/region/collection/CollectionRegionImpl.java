package ru.otus.cache.region.collection;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.CacheKeysFactory;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;

import ru.otus.cache.region.AbstractRegion;

/**
 * @author e.petrov. Created 08 - 2017.
 */
public class CollectionRegionImpl extends AbstractRegion implements CollectionRegion {

	private final CacheDataDescription metadata;

	public CollectionRegionImpl(CacheKeysFactory keysFactory, SessionFactoryOptions settings, String name, CacheDataDescription metadata) {
		super(keysFactory, settings, name);
		this.metadata = metadata;
	}

	@Override
	public CollectionRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
		switch ( accessType ) {
			case READ_ONLY: {
				return new ReadOnlyCollectionRegionAccessStrategy(this );
			}
			case READ_WRITE: {
				return new ReadWriteCollectionRegionAccessStrategy(this );
			}
			case NONSTRICT_READ_WRITE: {
				return new NonstrictReadWriteCollectionRegionAccessStrategy(this );
			}
			case TRANSACTIONAL: {
				return new TransactionalCollectionRegionAccessStrategy( this );
			}
			default: {
				throw new IllegalArgumentException( "unrecognized access strategy type [" + accessType + "]" );
			}
		}
	}

	@Override
	public boolean isTransactionAware() {
		return false;
	}

	@Override
	public CacheDataDescription getCacheDataDescription() {
		return metadata;
	}
}
