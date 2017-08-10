package ru.otus.service;


import java.io.Serializable;


/**
 * author: egor, created: 09.08.17.
 */
public interface Service<E, ID extends Serializable> {

    E save(E entity);

    Iterable<E> save(Iterable<E> entities);

    E load(ID id);

    Iterable<E> load(Iterable<ID> ids);

    Iterable<E> loadAll();

    boolean delete(E entity);

    boolean delete(Iterable<E> entities);

    boolean delete(ID id);
}
