package ru.otus.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;

import lombok.extern.slf4j.Slf4j;
import ru.otus.entity.UserEntity;
import ru.otus.repository.UserRepository;
import ru.otus.listener.CacheStateListener;

/**
 * author: egor, created: 09.08.17.
 */
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final LoadingCache<Long, UserEntity> cache;
    private final ReentrantReadWriteLock cacheLock;
	private final List<CacheStateListener> cacheStateListeners;
	private final AtomicBoolean cacheChangeFlag;

    public UserServiceImpl(UserRepository userRepository) {
        Objects.requireNonNull(userRepository, "Can't create CachedUserServiceImpl instance: user repository is null!");
        this.userRepository = userRepository;
	    this.cacheStateListeners = new CopyOnWriteArrayList<>();
        this.cacheLock = new ReentrantReadWriteLock();
	    this.cacheChangeFlag = new AtomicBoolean(false);
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(500)
	            .recordStats()
	            .removalListener(n -> {
	                log.warn("Removal cause {}", n.getCause());
	                cacheStateChanges();
                })
                .build(new CacheLoader<Long, UserEntity>() {
                    @Override
                    public UserEntity load(Long key) throws Exception {
	                    try {
		                    Objects.requireNonNull(key);
		                    return userRepository.findOne(key);
	                    } finally {
		                    cacheChangeFlag.compareAndSet(false, true);
	                    }
	                }

                    @Override
                    public Map<Long, UserEntity> loadAll(Iterable<? extends Long> keys) throws Exception {
	                    try {
		                    Objects.requireNonNull(keys);
		                    return StreamSupport.stream(userRepository.findAll(keys).spliterator(), false)
			                    .collect(Collectors.toMap(UserEntity::getId, Function.identity()));
	                    } finally {
		                    cacheChangeFlag.compareAndSet(false, true);
	                    }
	                }
                });
    }

    @Override
    public UserEntity loadByName(String name) {
	    cacheLock.writeLock().lock();
        try {
            Objects.requireNonNull(name);
	        UserEntity entity = userRepository.findByName(name);
	        cache.put(entity.getId(), entity);
	        cacheChangeFlag.compareAndSet(false, true);
            return entity;
        } catch (Exception e) {
            log.error("Can't load entity with name: " + name, e);
            return null;
        } finally {
	        if (cacheChangeFlag.compareAndSet(true, false)) {
		        cacheStateChanges();
	        }
	        cacheLock.writeLock().unlock();
        }
    }

    @Override
    public UserEntity save(UserEntity entity) {
	    cacheLock.writeLock().lock();
        try {
            Objects.requireNonNull(entity);
	        if (Objects.nonNull(entity.getId())) {
		        cache.invalidate(entity.getId());
	        }
	        entity = userRepository.save(entity);
	        cacheChangeFlag.compareAndSet(false, true);
	        return entity;
        } catch (Exception e) {
            log.error("Can't save user " + entity, e);
            return entity;
        } finally {
	        if (cacheChangeFlag.compareAndSet(true, false)) {
		        cacheStateChanges();
	        }
	        cacheLock.writeLock().unlock();
        }
    }

	@Override
	public Iterable<UserEntity> save(Iterable<UserEntity> entities) {
		cacheLock.writeLock().lock();
		try {
			List<UserEntity> nonNullEntities = StreamSupport.stream(entities.spliterator(), false)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
			Iterable<UserEntity> saved = userRepository.save(nonNullEntities);
			cache.putAll(StreamSupport.stream(saved.spliterator(), false).collect(Collectors.toMap(UserEntity::getId, Function.identity())));
			cacheChangeFlag.compareAndSet(false, true);
			return saved;
		} catch (Exception e) {
			log.error("Can't save users " + entities, e);
			return Collections.emptyList();
		} finally {
			if (cacheChangeFlag.compareAndSet(true, false)) {
				cacheStateChanges();
			}
			cacheLock.writeLock().unlock();
		}
	}

    @Override
    public UserEntity load(Long id) {
	    cacheLock.readLock().lock();
        try {
            Objects.requireNonNull(id);
            return cache.get(id);
        } catch (Exception e) {
            log.error("Can't load entity with id: " + id, e);
            return null;
        } finally {
	        if (cacheChangeFlag.compareAndSet(true, false)) {
		        cacheStateChanges();
	        }
	        cacheLock.readLock().unlock();
        }
    }

    @Override
    public Iterable<UserEntity> load(Iterable<Long> ids) {
	    cacheLock.readLock().lock();
        try {
            Objects.requireNonNull(ids);
            return cache.getAll(ids).values();
        } catch (Exception e) {
            log.error("Can't load entity with ids: " + ids, e);
            return Collections.emptyList();
        } finally {
	        if (cacheChangeFlag.compareAndSet(true, false)) {
		        cacheStateChanges();
	        }
	        cacheLock.readLock().unlock();
        }
    }

    @Override
    public Iterable<UserEntity> loadAll() {
        try {
            return userRepository.findAll(); //толстый запрос кэшировать не будем
        } catch (Exception e) {
            log.error("Can't load all users!", e);
            return Collections.emptyList();
        }
    }

    @Override
    public void delete(UserEntity entity) {
	    cacheLock.writeLock().lock();
	    try{
		    Objects.requireNonNull(entity);
		    Objects.requireNonNull(entity.getId());
		    cache.invalidate(entity.getId());
		    cacheChangeFlag.compareAndSet(false, true);
		    userRepository.delete(entity);
	    } catch (Exception e) {
		    log.error("Can't delete user!", e);
	    } finally {
		    if (cacheChangeFlag.compareAndSet(true, false)) {
			    cacheStateChanges();
		    }
		    cacheLock.writeLock().unlock();
	    }
	}

	public void registerListener(CacheStateListener listener) {
		if (Objects.nonNull(listener)) {
			listener.notifyStateChange(cacheState());
			cacheStateListeners.add(listener);
		}
	}

	private Map<String, Object> cacheState() {
		Map<String, Object> cacheState = new HashMap<>();
		cache.cleanUp();
		CacheStats cacheStats = cache.stats();
		cacheState.put(CacheStateListener.SIZE_KEY, cache.size());
		cacheState.put(CacheStateListener.HIT_COUNT_KEY, cacheStats.hitCount());
		cacheState.put(CacheStateListener.MISS_COUNT_KEY, cacheStats.missCount());
		cacheState.put(CacheStateListener.LOAD_COUNT_KEY, cacheStats.loadCount());
		cacheState.put(CacheStateListener.EVICTION_COUNT_KEY, cacheStats.evictionCount());
		return cacheState;
	}

	private void cacheStateChanges() {
		Map<String, Object> cacheState = cacheState();
		cacheStateListeners.parallelStream().forEach(l -> l.notifyStateChange(cacheState));
	}
}
