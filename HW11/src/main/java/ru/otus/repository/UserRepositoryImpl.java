package ru.otus.repository;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.persistence.EntityManager;

import ru.otus.entity.UserEntity;

/**
 * author: egor, created: 08.08.17.
 */
public class UserRepositoryImpl implements UserRepository {

    private final EntityManager em;

    public UserRepositoryImpl(EntityManager em) {
        Objects.requireNonNull(em, "Can't create UserRepositoryImpl instance: entity manager is null!");
        this.em = em;
    }

    @Override
    public UserEntity save(UserEntity entity) {
        return applyInTransaction(em -> em.merge(entity));
    }

    @Override
    public Iterable<UserEntity> save(Iterable<UserEntity> entities) {
        return applyInTransaction(em -> StreamSupport.stream(entities.spliterator(), false)
                .map(em::merge)
                .collect(Collectors.toList()));
    }

    @Override
    public boolean contains(Long id) {
        return findOne(id) != null;
    }

    @Override
    public UserEntity findOne(Long id) {
        return em.find(UserEntity.class, id);
    }

    @Override
    public Iterable<UserEntity> findAll() {
        return em.createQuery("SELECT u FROM UserEntity u", UserEntity.class).getResultList();
    }

    @Override
    public Iterable<UserEntity> findAll(Iterable<? extends Long> ids) {
        return em.createQuery("SELECT u FROM UserEntity u WHERE u.id IN (:ids)", UserEntity.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public UserEntity findByName(String name) {
        return em.createQuery("SELECT u FROM UserEntity u WHERE u.name = :name", UserEntity.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    @Override
    public long size() {
        return em.createQuery("SELECT count(u) FROM UserEntity u", Long.class)
                .getSingleResult();
    }

    @Override
    public void delete(UserEntity entity) {
        delete(Collections.singleton(entity));
    }

    @Override
    public void delete(Iterable<UserEntity> entities) {
        acceptInTransaction(em -> entities.forEach(e -> {
            if (e.getPhones() != null) {
                e.getPhones().clear();
            }
            em.remove(e);
        }));
    }

    @Override
    public EntityManager em() {
        return em;
    }
}
