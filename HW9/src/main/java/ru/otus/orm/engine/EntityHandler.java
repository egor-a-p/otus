package ru.otus.orm.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public class EntityHandler<T> {

	public static <T, C extends Class<T>> EntityHandler<T> create(C c) {
		return null;
	}


	private EntityHandler(T t) {

	}

	public Function<T, PreparedStatement> prepareSave(Connection connection) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("");
		return t -> setValue(statement, t);
	}

	public Function<Object, Supplier<T>> prepareLoad(Connection connection) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("");
		return t -> (() -> getValue(setId(statement, t)));
	}

	private PreparedStatement setValue(PreparedStatement preparedStatement, T t) {
		return null;
	}

	private PreparedStatement setId(PreparedStatement preparedStatement, Object id) {
		return null;
	}

	private T getValue(PreparedStatement preparedStatement) {
		return null;
	}
}
