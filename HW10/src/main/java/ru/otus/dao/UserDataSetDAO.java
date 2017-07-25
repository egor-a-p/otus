package ru.otus.dao;

import ru.otus.entity.UserDataSet;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public interface UserDataSetDAO extends DataSetDAO<UserDataSet> {

	UserDataSet readByName(String name);

}
