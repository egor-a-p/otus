package ru.otus.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

/**
 * author: egor, created: 09.08.17.
 */
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final LoadingCache<Long, UserEntity> cache;
    private final ReentrantReadWriteLock cacheLock;

    public UserServiceImpl(UserRepository userRepository) {
        Objects.requireNonNull(userRepository, "Can't create CachedUserServiceImpl instance: user repository is null!");
        this.userRepository = userRepository;
        this.cacheLock = new ReentrantReadWriteLock();
        this.cache = CacheBuilder.newBuilder()
                .softValues()
                .recordStats()
                .removalListener(n -> log.debug("Removal cause {}", n.getCause()))
                .build(new CacheLoader<Long, UserEntity>() {
                    @Override
                    public UserEntity load(Long key) throws Exception {
                        Objects.requireNonNull(key);
                        return userRepository.findOne(key);
                    }

                    @Override
                    public Map<Long, UserEntity> loadAll(Iterable<? extends Long> keys) throws Exception {
                        Objects.requireNonNull(keys);
                        return StreamSupport.stream(userRepository.findAll(keys).spliterator(), false)
                                .collect(Collectors.toMap(UserEntity::getId, Function.identity()));
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
            return entity;
        } catch (Exception e) {
            log.error("Can't load entity with name: " + name, e);
            return null;
        } finally {
	        cacheLock.writeLock().unlock();
        }
    }

    @Override
    public UserEntity save(UserEntity entity) {
	    cacheLock.writeLock().lock();
        try {
            Objects.requireNonNull(entity);
            entity = userRepository.save(entity);
	        cache.put(entity.getId(), entity);
            return entity;
        } catch (Exception e) {
            log.error("Can't save user " + entity, e);
            return entity;
        } finally {
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
			return saved;
		} catch (Exception e) {
			log.error("Can't save users " + entities, e);
			return Collections.emptyList();
		} finally {
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
		    userRepository.delete(entity);
	    } catch (Exception e) {
		    log.error("Can't delete user!", e);
	    } finally {
		    cacheLock.writeLock().unlock();
	    }
	}

	public CacheStats cacheStatistics() {
        cache.cleanUp();
        return cache.stats();
    }
}
