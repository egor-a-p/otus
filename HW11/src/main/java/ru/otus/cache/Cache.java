package ru.otus.cache;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * author: egor, created: 08.08.17.
 */
public interface Cache<K, V> {

    V put(K key, V value);

    boolean contains(K key);

    V get(K key);

    V replace(K key, Function<? extends V, ? extends V> replaceFunction);

    V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction);

    V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction);
}
