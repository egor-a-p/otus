package ru.otus.orm;

import ru.otus.executor.Executor;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public class SimpleExecutor implements Executor {
	@Override
	public <T> void save(T t) {
		
	}

	@Override
	public <T> T load(Class<T> tClass, Object id) {
		return null;
	}
}
