package ru.otus.service;

import ru.otus.entity.UserEntity;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public interface UserDBService extends DBService<UserEntity> {

	UserEntity readByName(String name);

}
