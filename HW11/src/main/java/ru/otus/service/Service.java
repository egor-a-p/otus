package ru.otus.service;


import com.google.common.cache.CacheStats;

import java.io.Serializable;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * author: egor, created: 09.08.17.
 */
public interface Service<E, ID extends Serializable> {

    E save(E entity);

    default Iterable<E> save(Iterable<E> entities) {
        return StreamSupport.stream(entities.spliterator(), false).map(this::save).collect(Collectors.toList());
    }

    E load(ID id);

    default Iterable<E> load(Iterable<ID> ids) {
        return StreamSupport.stream(ids.spliterator(), false).map(this::load).collect(Collectors.toList());
    }

    Iterable<E> loadAll();

    void delete(E entity);

    default void delete(Iterable<E> entities) {
        entities.forEach(this::delete);
    }

    void delete(ID id);

    void deleteAll();

}
