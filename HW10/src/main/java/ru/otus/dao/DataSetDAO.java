package ru.otus.dao;

import java.util.List;

import ru.otus.entity.DataSet;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public interface DataSetDAO<T extends DataSet> {

	 void save(T dataSet);

	T read(long id);

	T readByName(String name);

	List<T> readAll();

}
