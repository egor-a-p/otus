package ru.otus.service;

import com.google.common.cache.*;
import lombok.extern.slf4j.Slf4j;
import ru.otus.entity.UserEntity;
import ru.otus.repository.UserRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * author: egor, created: 09.08.17.
 */
@Slf4j
public class CachedUserServiceImpl implements CachedUserService {

    private final UserRepository userRepository;
    private final LoadingCache<Long, UserEntity> cache;

    public CachedUserServiceImpl(UserRepository userRepository) {
        Objects.requireNonNull(userRepository, "Can't create CachedUserServiceImpl instance: user repository is null!");
        this.userRepository = userRepository;
        this.cache = CacheBuilder.newBuilder()
                .softValues()
                .recordStats()
                .removalListener(n -> {
                    if (log.isDebugEnabled() && RemovalCause.COLLECTED == n.getCause()) {
                        log.debug("GC collect entity!");
                    }
                })
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
    public Iterable<UserEntity> loadByName(String name) {
        try {
            Objects.requireNonNull(name);
            return userRepository.findByName(name);
        } catch (Exception e) {
            log.error("Can't load entity with name: " + name, e);
            return Collections.emptyList();
        }
    }

    @Override
    public UserEntity save(UserEntity entity) {
        try {
            Objects.requireNonNull(entity);
            if (entity.getId() != null) {
                cache.invalidate(entity.getId());
            }
            entity = userRepository.save(entity);
            cache.put(entity.getId(), entity);
            return entity;
        } catch (Exception e) {
            log.error("Can't save user " + entity, e);
            return entity;
        }
    }

    @Override
    public UserEntity load(Long id) {
        try {
            Objects.requireNonNull(id);
            return cache.get(id);
        } catch (Exception e) {
            log.error("Can't load entity with id: " + id, e);
            return null;
        }
    }

    @Override
    public Iterable<UserEntity> load(Iterable<Long> ids) {
        try {
            Objects.requireNonNull(ids);
            return cache.getAll(ids).values();
        } catch (Exception e) {
            log.error("Can't load entity with ids: " + ids, e);
            return Collections.emptyList();
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
        if (entity == null) {
            log.error("Can't delete user, entity is null!");
            return;
        }
        delete(entity.getId());
    }

    @Override
    public void delete(Long id) {
        try {
            Objects.requireNonNull(id);
            cache.invalidate(id);
            userRepository.delete(id);
        } catch (Exception e) {
            log.error("Can't delete by id " + id, e);
        }
    }

    @Override
    public void deleteAll() {
        cache.invalidateAll();
        userRepository.clear();
    }

    @Override
    public CacheStats cacheStatistics() {
        cache.cleanUp();
        return cache.stats();
    }
}
