package ru.otus.service;



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

    Iterable<E> load(Iterable<ID> ids);

    Iterable<E> loadAll();

    void delete(E entity);

    default void delete(Iterable<E> entities) {
        entities.forEach(this::delete);
    }

}
