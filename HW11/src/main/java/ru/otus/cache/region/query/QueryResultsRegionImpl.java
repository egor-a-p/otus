package ru.otus.cache.region.query;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.spi.CacheKeysFactory;
import org.hibernate.cache.spi.QueryResultsRegion;

import ru.otus.cache.region.AbstractRegion;

/**
 * @author e.petrov. Created 08 - 2017.
 */
public class QueryResultsRegionImpl extends AbstractRegion implements QueryResultsRegion {
	public QueryResultsRegionImpl(CacheKeysFactory keysFactory, SessionFactoryOptions settings, String name) {
		super(keysFactory, settings, name);
	}
}
