package ru.otus.service;

import java.util.List;

import ru.otus.entity.UserDataSet;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public class DBServiceHibernateImpl implements UserDBService {




	@Override
	public String getLocalStatus() {
		return null;
	}

	@Override
	public void save(UserDataSet dataSet) {

	}

	@Override
	public UserDataSet read(long id) {
		return null;
	}

	@Override
	public List<UserDataSet> readAll() {
		return null;
	}

	@Override
	public void shutdown() {

	}

	@Override
	public UserDataSet readByName(String name) {
		return null;
	}
}
