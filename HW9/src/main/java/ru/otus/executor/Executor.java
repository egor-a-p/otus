package ru.otus.executor;

import java.sql.SQLException;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public interface Executor {
	<T> void save(T t) throws SQLException;

	<T> T load(Class<T> tClass, Object id) throws SQLException;
}
