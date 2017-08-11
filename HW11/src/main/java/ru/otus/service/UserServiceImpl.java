package ru.otus.service;

import ru.otus.cache.Cache;
import ru.otus.entity.UserEntity;
import ru.otus.repository.UserRepository;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * author: egor, created: 09.08.17.
 */
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Cache<Long, UserEntity> cache;

    public UserServiceImpl(UserRepository userRepository, Cache<Long, UserEntity> cache) {
        Objects.requireNonNull(userRepository, "Can't create UserServiceImpl instance: user repository is null!");
        Objects.requireNonNull(cache, "Can't create UserServiceImpl instance: cache is null!");
        this.userRepository = userRepository;
        this.cache = cache;
    }

    @Override
    public Iterable<UserEntity> loadByName(String name) {
        return null;
    }

    @Override
    public UserEntity save(UserEntity entity) {
        Objects.requireNonNull(entity);
        UserEntity result;
        cache.put((result = userRepository.save(entity)).getId(), result);
        return result;
    }

    @Override
    public Iterable<UserEntity> save(Iterable<UserEntity> entities) {
        return StreamSupport.stream(userRepository.save(entities).spliterator(), false)
                .map(e -> cache.replace(e.getId(), old -> e))
                .collect(Collectors.toList());
    }

    @Override
    public UserEntity load(Long id) {
        return cache.computeIfAbsent(id, userRepository::findOne);
    }

    @Override
    public Iterable<UserEntity> load(Iterable<Long> ids) {
        return userRepository.findAll(ids);
    }

    @Override
    public Iterable<UserEntity> loadAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean delete(UserEntity entity) {
        userRepository.delete(entity);
        return true;
    }

    @Override
    public boolean delete(Iterable<UserEntity> entities) {
        userRepository.delete(entities);
        return true;
    }

    @Override
    public boolean delete(Long id) {
        userRepository.delete(id);
        return true;
    }
}
