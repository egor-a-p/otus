package ru.otus.cache;

/**
 * author: egor, created: 08.08.17.
 */
public interface Cache<K, V> {

    boolean put(K key, V value);

    boolean contains(K key);

    V get(K key);


}
