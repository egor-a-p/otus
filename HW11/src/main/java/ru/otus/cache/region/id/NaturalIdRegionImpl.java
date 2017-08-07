package ru.otus.cache.region.id;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.CacheKeysFactory;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;

import ru.otus.cache.region.AbstractRegion;

/**
 * @author e.petrov. Created 08 - 2017.
 */
public class NaturalIdRegionImpl extends AbstractRegion implements NaturalIdRegion {
	private final CacheDataDescription metadata;

	public NaturalIdRegionImpl(CacheKeysFactory keysFactory, SessionFactoryOptions settings, String name, CacheDataDescription metadata) {
		super(keysFactory, settings, name);
		this.metadata = metadata;
	}

	@Override
	public NaturalIdRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
		switch ( accessType ) {
			case READ_ONLY:
				return new ReadOnlyNaturalIdRegionAccessStrategy( this );
			case READ_WRITE:
				return new ReadWriteNaturalIdRegionAccessStrategy( this );
			case NONSTRICT_READ_WRITE:
				return new NonstrictReadWriteNaturalIdRegionAccessStrategy( this );
			case TRANSACTIONAL:
				return new TransactionalNaturalIdRegionAccessStrategy( this );
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
