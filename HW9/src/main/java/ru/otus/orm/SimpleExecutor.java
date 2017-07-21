package ru.otus.orm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.sql.DataSource;

import ru.otus.executor.Executor;
import ru.otus.orm.db.DataBase;
import ru.otus.orm.engine.EntityHandler;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public class SimpleExecutor implements Executor {

	private final DataSource dataSource = DataBase.getDataSource();

	private static final ConcurrentMap<Class, EntityHandler> REGISTERED_HANDLERS = new ConcurrentHashMap<>();

	@Override
	@SuppressWarnings("unchecked")
	public <V> void save(V v) throws SQLException {
		EntityHandler<V> entityHandler = REGISTERED_HANDLERS.computeIfAbsent(v.getClass(), EntityHandler::create);
		try(Connection connection = dataSource.getConnection()) {
			connection.setAutoCommit(false);
			entityHandler.prepareSave(connection).apply(v).executeUpdate();
			connection.commit();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <V> V load(Class<V> vClass, Object id) throws SQLException {
		EntityHandler<V> entityHandler = REGISTERED_HANDLERS.computeIfAbsent(vClass, EntityHandler::create);
		try(Connection connection = dataSource.getConnection()) {
			return entityHandler.prepareLoad(connection).apply(id).get();
		}
	}
}
