package ru.otus.executor;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public interface Executor {
	<T> T save(T t);

	<T> T load(Class<T> tClass, Object id);
}
