package ru.otus.repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * author: egor, created: 09.08.17.
 */
public interface JPARepository<E, ID extends Serializable> extends Repository<E, ID> {

    EntityManager em();

    default <R> R applyInTransaction(Function<EntityManager, R> function) {
        em().getTransaction().begin();
        R r = function.apply(em());
        em().getTransaction().commit();
        return r;
    }

    default void acceptInTransaction(Consumer<EntityManager> consumer) {
        em().getTransaction().begin();
        consumer.accept(em());
        em().getTransaction().commit();
    }
}
