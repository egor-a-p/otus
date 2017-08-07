package ru.otus.cache.region.entity;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.CacheKeysFactory;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;

import ru.otus.cache.region.AbstractRegion;


/**
 * @author e.petrov. Created 08 - 2017.
 */
public class EntityRegionImpl extends AbstractRegion implements EntityRegion {
	private final CacheDataDescription metadata;

	public EntityRegionImpl(CacheKeysFactory keysFactory, SessionFactoryOptions settings, String name, CacheDataDescription metadata) {
		super(keysFactory, settings, name);
		this.metadata = metadata;
	}

	@Override
	public EntityRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
		switch ( accessType ) {
			case READ_ONLY:
				return new ReadOnlyEntityRegionAccessStrategy( this );
			case READ_WRITE:
				return new ReadWriteEntityRegionAccessStrategy( this );
			case NONSTRICT_READ_WRITE:
				return new NonstrictReadWriteEntityRegionAccessStrategy( this );
			case TRANSACTIONAL:
				return new TransactionalEntityRegionAccessStrategy( this );

			default:
				throw new IllegalArgumentException( "unrecognized access strategy type [" + accessType + "]" );
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
