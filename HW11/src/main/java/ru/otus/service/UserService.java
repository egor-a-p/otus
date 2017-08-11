package ru.otus.service;

import ru.otus.entity.UserEntity;


/**
 * author: egor, created: 08.08.17.
 */
public interface UserService extends Service<UserEntity, Long> {

    Iterable<UserEntity> loadByName(String name);

}
