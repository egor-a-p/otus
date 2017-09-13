package ru.otus.repository;

import java.io.Serializable;
import java.util.Iterator;

/**
 * author: egor, created: 08.08.17.
 */
public interface Repository<E, ID extends Serializable> extends Iterable<E> {

    E save(E entity);

    Iterable<E> save(Iterable<E> entities);

    boolean contains(ID id);

    E findOne(ID id);

    Iterable<E> findAll();

    Iterable<E> findAll(Iterable<? extends ID> ids);

    long size();

    void delete(E entity);

    void delete(Iterable<E> entities);

    @Override
    default Iterator<E> iterator() {
        return findAll().iterator();
    }
}
