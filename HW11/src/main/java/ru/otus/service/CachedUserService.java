package ru.otus.service;

import com.google.common.cache.CacheStats;
import ru.otus.entity.UserEntity;


/**
 * author: egor, created: 08.08.17.
 */
public interface CachedUserService extends Service<UserEntity, Long> {

    Iterable<UserEntity> loadByName(String name);

    CacheStats cacheStatistics();

}
