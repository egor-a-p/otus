package ru.otus.repository;

import ru.otus.entity.UserEntity;

/**
 * author: egor, created: 09.08.17.
 */
public interface UserRepository extends JPARepository<UserEntity, Long> {

    UserEntity findByName(String name);

}
