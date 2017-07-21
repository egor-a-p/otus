package ru.otus.orm.engine;

import java.lang.reflect.*;
import java.util.Objects;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public class EntityMapper {


	public <T> void map(Class<T> tClass) {
		Objects.requireNonNull(tClass);

		for (Class<?> c = tClass; c != null; c = c.getSuperclass()) {
			for (Field f : c.getDeclaredFields()) {

			}
		}

	}

}
