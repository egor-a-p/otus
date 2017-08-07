package ru.otus.cache.region;

import java.util.Map;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheKeysFactory;
import org.hibernate.cache.spi.GeneralDataRegion;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.extern.slf4j.Slf4j;
import ru.otus.memory.util.MemoryUtil;

/**
 * @author e.petrov. Created 08 - 2017.
 */
@Slf4j
public abstract class AbstractRegion implements GeneralDataRegion {
	private final CacheKeysFactory keysFactory;
	private final SessionFactoryOptions settings;
	private final String name;
	private final Cache cache;

	public AbstractRegion(CacheKeysFactory keysFactory, SessionFactoryOptions settings, String name) {
		this.keysFactory = keysFactory;
		this.settings = settings;
		this.name = name;
		this.cache = CacheBuilder.newBuilder().softValues().build();
	}

	@Override
	public Object get(SharedSessionContractImplementor session, Object key) throws CacheException {
		log.debug("Cache{} get: key - {}", name, key);
		if (key == null) {
			return null;
		}
		return cache.getIfPresent(key);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void put(SharedSessionContractImplementor session, Object key, Object value) throws CacheException {
		log.debug("Cache{} put: key - {}, value - {}", name, key, value);
		if (key != null && value != null) {
			cache.put(key, value);
		}
	}

	@Override
	public void evict(Object key) throws CacheException {
		log.debug("Cache{} evict: key - {}", name, key);
		if (key != null) {
			cache.invalidate(key);
		}
	}

	@Override
	public void evictAll() throws CacheException {
		log.debug("Cache{} evictAll", name);
		cache.invalidateAll();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void destroy() throws CacheException {
		log.debug("Cache{} destroy", name);
		cache.invalidateAll();
	}

	@Override
	public boolean contains(Object key) {
		log.debug("Cache{} contains: key - {}", name, key);
		return key != null && cache.asMap().containsKey(key);
	}

	@Override
	public long getSizeInMemory() {
		return MemoryUtil.sizeOf(cache.asMap().values());
	}

	@Override
	public long getElementCountInMemory() {
		return cache.size();
	}

	@Override
	public long getElementCountOnDisk() {
		return 0;
	}

	@Override
	public Map toMap() {
		return cache.asMap();
	}

	@Override
	public long nextTimestamp() {
		return 0;
	}

	@Override
	public int getTimeout() {
		return 0;
	}

	public CacheKeysFactory getKeysFactory() {
		return keysFactory;
	}

	public SessionFactoryOptions getSettings() {
		return settings;
	}
}
