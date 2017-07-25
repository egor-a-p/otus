package ru.otus.orm.engine;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import javafx.util.Pair;
import ru.otus.orm.ORMException;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public class EntityHandler<T> {

	public static <T> EntityHandler<T> create(Class<T> tClass) {
		if(tClass.getDeclaredAnnotation(Entity.class) == null) {
			throw new IllegalArgumentException();
		}

		Table table = tClass.getDeclaredAnnotation(Table.class);

		if (table == null || StringUtils.isBlank(table.name())) {
			throw new IllegalArgumentException();
		}

		return new EntityHandler<>(tClass, table.name());
	}

	private Class<T> tClass;
	private String tableName;
	private Pair<String, Field> id;
	private List<Pair<String, Field>> fields;
	private String select;
	private String insert;
	private String update;

	public Function<T, Supplier<T>> prepareSave(Connection connection) {
		return t ->(() -> {
			try {
				boolean hasId = hasId(t);
				connection.setAutoCommit(false);
				try (PreparedStatement statement = hasId ?
					connection.prepareStatement(update) :
					connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
					if (hasId) {
						if (fillUpdate(statement, t).executeUpdate() == 0) {
							throw new ORMException("Entity " + t + "have illegal id.");
						}
					} else {
						fillInsert(statement, t).executeUpdate();
						ResultSet generatedKeys = statement.getGeneratedKeys();
						if (generatedKeys.next()) {
							id.getValue().set(t, generatedKeys.getObject(1));
						} else {
							throw new ORMException("Insert new entity failed, no id obtained.");
						}
					}
				} catch (ORMException | SQLException e) {
					connection.rollback();
					throw e;
				}
				connection.commit();
				return t;
			} catch (SQLException | IllegalAccessException e) {
				throw new ORMException(e);
			}
		});
	}

	public Function<Object, Supplier<T>> prepareLoad(Connection connection) {
		return t -> (() -> {
			try {
				return getValue(fillSelect(connection.prepareStatement(select), t));
			} catch (SQLException | IllegalAccessException | InstantiationException e) {
				throw new ORMException(e);
			}
		});
	}

	private EntityHandler(Class<T> tClass, String tableName) {
		this.tClass = tClass;
		this.tableName = tableName;
		map(tClass);
		initInsert();
		initSelect();
		initUpdate();
	}

	private void map(Class<T> tClass) {
		Set<Pair<String, Field>> fields = new LinkedHashSet<>();
		for (Class<?> c = tClass; c != null; c = c.getSuperclass()) {
			for (Field f : c.getDeclaredFields()) {
				Transient aTransient = f.getAnnotation(Transient.class);
				if (aTransient != null) {
					continue;
				}

				Id id = f.getAnnotation(Id.class);
				Column column = f.getAnnotation(Column.class);
				if (id != null) {
					if (this.id != null) {
						throw new ORMException("Illegal entity: too many id fields.");
					}
					f.setAccessible(true);
					String columnName = column != null && StringUtils.isNotBlank(column.name()) ? column.name() : f.getName();
					this.id = new Pair<>(columnName, f);
					continue;
				}

				if (column != null) {
					if (StringUtils.isBlank(column.name())) {
						throw new ORMException("Illegal entity: column name is empty.");
					}
					f.setAccessible(true);
					if(!fields.add(new Pair<>(column.name(), f))) {
						throw new ORMException("Illegal entity: same field's.");
					}
				}

				ManyToOne manyToOne = f.getAnnotation(ManyToOne.class);
				OneToOne oneToOne = f.getAnnotation(OneToOne.class);
				OneToMany oneToMany = f.getAnnotation(OneToMany.class);
				ManyToMany manyToMany = f.getAnnotation(ManyToMany.class);

				if (manyToMany != null || oneToMany != null || oneToOne != null|| manyToOne != null) {
					throw new UnsupportedOperationException("Relationship's doesn't supported.");
				}
			}
		}

		if (id == null) {
			throw new ORMException("Illegal entity: should contain id field.");
		}

		this.fields = new ArrayList<>(fields);
	}

	private void initUpdate() {
		StringBuilder update = new StringBuilder("UPDATE ").append(tableName).append(" SET ").append(id.getKey()).append(" = ?");
		fields.forEach(f -> update.append(", ").append(f.getKey()).append(" = ?"));
		this.update = update.append(" WHERE ").append(id.getKey()).append(" = ?").toString();
	}

	private void initInsert() {
		StringBuilder insert = new StringBuilder("INSERT INTO ").append(tableName).append("(");
		fields.forEach(f -> insert.append(f.getKey()).append(","));
		insert.deleteCharAt(insert.length() - 1).append(") VALUES (");
		fields.forEach(f -> insert.append("?").append(","));
		this.insert = insert.deleteCharAt(insert.length() - 1).append(")").toString();
	}

	private void initSelect() {
		StringBuilder select = new StringBuilder("SELECT ").append(id.getKey());
		fields.forEach(f -> select.append(", ").append(f.getKey()));
		this.select = select.append(" FROM ").append(tableName).append(" WHERE ").append(id.getKey()).append(" = ?").toString();
	}

	private PreparedStatement fillInsert(PreparedStatement preparedStatement, T t) throws IllegalAccessException, SQLException {
		for (int i = 1; i <= fields.size(); i++) {
			preparedStatement.setObject(i, fields.get(i - 1).getValue().get(t));
		}

		return preparedStatement;
	}

	private PreparedStatement fillUpdate(PreparedStatement preparedStatement, T t) throws IllegalAccessException, SQLException {
		preparedStatement.setObject(1, id.getValue().get(t));
		for (int i = 1; i <= fields.size(); i++) {
			preparedStatement.setObject(i + 1, fields.get(i - 1).getValue().get(t));
		}
		preparedStatement.setObject(fields.size() + 2, id.getValue().get(t));

		return preparedStatement;
	}

	private PreparedStatement fillSelect(PreparedStatement preparedStatement, Object id) throws SQLException {
		preparedStatement.setObject(1, id);
		return preparedStatement;
	}

	private T getValue(PreparedStatement preparedStatement) throws SQLException, IllegalAccessException, InstantiationException {
		ResultSet resultSet = preparedStatement.executeQuery();

		if (!resultSet.next()) {
			return null;
		}

		T instance = tClass.newInstance();
		id.getValue().set(instance, resultSet.getObject(1));

		for (int i = 1; i <= fields.size(); i++) {
			fields.get(i - 1).getValue().set(instance, resultSet.getObject(i + 1));
		}

		return instance;
	}

	private boolean hasId(T t) throws IllegalAccessException {
		return id.getValue().get(t) != null;
	}
}
