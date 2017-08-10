package ru.otus.service;

import ru.otus.cache.Cache;
import ru.otus.entity.UserEntity;
import ru.otus.repository.Repository;
import ru.otus.repository.UserRepository;

import java.util.Objects;

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
        return userRepository.findByName(name);
    }

    @Override
    public UserEntity save(UserEntity entity) {
        return userRepository.save(entity);
    }

    @Override
    public Iterable<UserEntity> save(Iterable<UserEntity> entities) {
        return userRepository.save(entities);
    }

    @Override
    public UserEntity load(Long id) {
        return userRepository.findOne(id);
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

    @Override
    public Cache<Long, UserEntity> cache() {
        return cache;
    }

    @Override
    public Repository<UserEntity, Long> repository() {
        return userRepository;
    }
}
