package ru.otus.service;

import ru.otus.entity.UserDataSet;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public interface UserDBService extends DBService<UserDataSet> {
	UserDataSet readByName(String name);
}
