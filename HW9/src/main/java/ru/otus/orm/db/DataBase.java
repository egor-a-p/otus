package ru.otus.orm.db;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public class DataBase {
	private static final String DB_PROPERTIES = "/db.properties";

	private static volatile DataSource dataSource;

	public static DataSource getDataSource() {
		if (dataSource == null) {
			synchronized (DataBase.class) {
				if (dataSource == null) {
					dataSource = new HikariDataSource(new HikariConfig(DB_PROPERTIES));
				}
			}
		}
		return dataSource;
	}
}
