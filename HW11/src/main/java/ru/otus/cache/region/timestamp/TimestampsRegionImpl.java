package ru.otus.cache.region.timestamp;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.spi.CacheKeysFactory;
import org.hibernate.cache.spi.TimestampsRegion;

import ru.otus.cache.region.AbstractRegion;

/**
 * @author e.petrov. Created 08 - 2017.
 */
public class TimestampsRegionImpl extends AbstractRegion implements TimestampsRegion {
	public TimestampsRegionImpl(CacheKeysFactory keysFactory, SessionFactoryOptions settings, String name) {
		super(keysFactory, settings, name);
	}
}
