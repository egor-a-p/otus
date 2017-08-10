package ru.otus.service;

import java.util.List;

import ru.otus.entity.BaseEntity;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public interface DBService<T extends BaseEntity> {
	String getLocalStatus();

	void save(T dataSet);

	T read(long id);

	List<T> readAll();

	void shutdown();
}
