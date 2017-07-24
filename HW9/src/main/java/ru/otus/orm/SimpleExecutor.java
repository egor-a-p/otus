package ru.otus.orm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
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

	private static final ConcurrentMap<Class, EntityHandler> REGISTERED_HANDLERS = new ConcurrentHashMap<>();

	public static Executor getInstance() {
		return new SimpleExecutor();
	}

	private final DataSource dataSource;

	private SimpleExecutor() {
		dataSource = DataBase.getDataSource();
	}

	@Override
	public <V> V save(V v) throws ORMException {
		Objects.requireNonNull(v);
		EntityHandler<V> entityHandler = getHandler(v.getClass());
		try(Connection connection = dataSource.getConnection()) {
			connection.setAutoCommit(false);
			v = entityHandler.prepareSave(connection).apply(v).get();
			connection.commit();
			return v;
		} catch (SQLException e) {
			throw new ORMException(e);
		}
	}


	@Override
	public <V> V load(Class<V> vClass, Object id) throws ORMException {
		Objects.requireNonNull(id);
		Objects.requireNonNull(vClass);
		EntityHandler<V> entityHandler = getHandler(vClass);
		try(Connection connection = dataSource.getConnection()) {
			return entityHandler.prepareLoad(connection).apply(id).get();
		} catch (SQLException e) {
			throw new ORMException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private <V> EntityHandler<V> getHandler(Class<?> vClass) {
		return REGISTERED_HANDLERS.computeIfAbsent(vClass, EntityHandler::create);
	}
}
