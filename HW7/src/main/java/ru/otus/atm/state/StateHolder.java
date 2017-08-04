package ru.otus.atm.state;

/**
 * @author e.petrov. Created 08 - 2017.
 */
public interface StateHolder<T> {

	void save(T t);

	void restore(T t);

}
