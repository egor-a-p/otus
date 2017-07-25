package ru.otus.dao;

import java.util.List;
import java.util.function.Function;

import org.hibernate.Session;
import ru.otus.entity.DataSet;

import javax.persistence.EntityManager;

/**
 * @author e.petrov. Created 07 - 2017.
 */
interface DataSetDAO<T extends DataSet> {

    String status();

	void save(T dataSet);

	T read(long id);

	List<T> readAll();

	void close();
}

