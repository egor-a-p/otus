package ru.otus.dao;

import java.util.List;

import ru.otus.entity.BaseEntity;


/**
 * @author e.petrov. Created 07 - 2017.
 */
public interface DataSetDAO<T extends BaseEntity> {

    String status();

	T save(T dataSet);

	T read(long id);

	List<T> readAll();

	void close();
}

