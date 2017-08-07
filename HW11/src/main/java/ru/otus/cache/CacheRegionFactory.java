package ru.otus.cache;

import java.util.Properties;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.internal.DefaultCacheKeysFactory;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.CacheKeysFactory;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.QueryResultsRegion;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.cache.spi.TimestampsRegion;
import org.hibernate.cache.spi.access.AccessType;

import lombok.extern.slf4j.Slf4j;
import ru.otus.cache.region.collection.CollectionRegionImpl;
import ru.otus.cache.region.entity.EntityRegionImpl;
import ru.otus.cache.region.id.NaturalIdRegionImpl;
import ru.otus.cache.region.query.QueryResultsRegionImpl;
import ru.otus.cache.region.timestamp.TimestampsRegionImpl;

/**
 * @author e.petrov. Created 08 - 2017.
 */
@Slf4j
public class CacheRegionFactory implements RegionFactory {
	public static String ACCESS_TYPE_KEY = "DefaultAccessType";

	private final CacheKeysFactory keysFactory;

	private SessionFactoryOptions settings;
	private Properties properties;

	public CacheRegionFactory() {
		this(null);
	}

	public CacheRegionFactory(Properties properties) {
		this(DefaultCacheKeysFactory.INSTANCE, properties);
	}

	CacheRegionFactory(CacheKeysFactory keysFactory, Properties properties) {
		this.keysFactory = keysFactory;
		this.properties = properties;
	}

	@Override
	public void start(SessionFactoryOptions settings, Properties properties) throws CacheException {
		this.settings = settings;
		this.properties = properties;
	}

	@Override
	public void stop() {
	}

	@Override
	public boolean isMinimalPutsEnabledByDefault() {
		return false;
	}

	@Override
	public AccessType getDefaultAccessType() {
		if (properties != null && properties.get(ACCESS_TYPE_KEY) != null) {
			return AccessType.fromExternalName(properties.getProperty(ACCESS_TYPE_KEY));
		}
		return AccessType.READ_WRITE;
	}

	@Override
	public long nextTimestamp() {
		return 0;
	}

	@Override
	public EntityRegion buildEntityRegion(String regionName, Properties properties, CacheDataDescription metadata) throws CacheException {
		return new EntityRegionImpl(keysFactory, settings, regionName, metadata);
	}

	@Override
	public NaturalIdRegion buildNaturalIdRegion(String regionName, Properties properties, CacheDataDescription metadata) throws CacheException {
		return new NaturalIdRegionImpl(keysFactory, settings, regionName, metadata);
	}

	@Override
	public CollectionRegion buildCollectionRegion(String regionName, Properties properties, CacheDataDescription metadata) throws CacheException {
		return new CollectionRegionImpl(keysFactory, settings, regionName, metadata);
	}

	@Override
	public QueryResultsRegion buildQueryResultsRegion(String regionName, Properties properties) throws CacheException {
		return new QueryResultsRegionImpl(keysFactory, settings, regionName);
	}

	@Override
	public TimestampsRegion buildTimestampsRegion(String regionName, Properties properties) throws CacheException {
		return new TimestampsRegionImpl(keysFactory, settings, regionName);
	}
}
