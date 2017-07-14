package ru.otus.orm.engine.types;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import javax.activation.UnsupportedDataTypeException;

import javafx.util.Pair;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public enum SQLType {
	BIGINT("bigint", long.class, Long.class),
	INT("int", int.class, Integer.class),
	VARCHAR("varchar", String.class);

	private static final ConcurrentMap<Class, SQLType> sqlTypeByJavaClasses = Arrays.stream(values())
		.flatMap(sqlType -> sqlType.classes.stream().map(c -> new Pair<>(c, sqlType)))
		.collect(Collectors.toConcurrentMap(Pair::getKey, Pair::getValue));

	public static SQLType map(Class aClass) throws UnsupportedDataTypeException {
		return Optional.ofNullable(sqlTypeByJavaClasses.get(aClass)).orElseThrow(UnsupportedDataTypeException::new);
	}

	private final String declaration;
	private final List<Class> classes;

	SQLType(String declaration, Class... classes) {
		Objects.requireNonNull(declaration);
		Objects.requireNonNull(classes);
		this.declaration = declaration;
		this.classes = Arrays.asList(classes);
	}

	public String declaration() {
		return declaration;
	}
}
