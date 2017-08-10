package ru.otus.service;

import ru.otus.cache.Cache;
import ru.otus.repository.Repository;

import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * author: egor, created: 09.08.17.
 */
public interface CachingService<E, ID extends Serializable> extends Service<E, ID> {

    Cache<ID,E> cache();

    Repository<E, ID> repository();

    default <R> R cachingApply(BiFunction<Cache<ID, E>, Repository<E, ID>, R> function) {
        return function.apply(cache(), repository());
    }

    default void cachingAccept(BiConsumer<Cache<ID, E>, Repository<E, ID>> consumer) {
       consumer.accept(cache(), repository());
    }
}
