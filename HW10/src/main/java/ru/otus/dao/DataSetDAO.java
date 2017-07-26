package ru.otus.dao;

import java.util.List;

import ru.otus.entity.DataSet;


/**
 * @author e.petrov. Created 07 - 2017.
 */
public interface DataSetDAO<T extends DataSet> {

    String status();

	void save(T dataSet);

	T read(long id);

	List<T> readAll();

	void close();
}

