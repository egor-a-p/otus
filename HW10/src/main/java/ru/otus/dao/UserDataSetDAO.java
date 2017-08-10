package ru.otus.dao;

import ru.otus.entity.UserEntity;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public interface UserDataSetDAO extends DataSetDAO<UserEntity> {

	UserEntity readByName(String name);

}
