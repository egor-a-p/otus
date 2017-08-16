package ru.otus.repository;

import javax.persistence.criteria.CriteriaQuery;
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

    void delete(ID id);

    void delete(E entity);

    void delete(Iterable<E> entities);

    void clear();

    @Override
    default Iterator<E> iterator() {
        return findAll().iterator();
    }
}
