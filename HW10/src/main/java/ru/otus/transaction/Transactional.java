package ru.otus.transaction;

import java.util.function.Function;

import javax.persistence.EntityManager;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public interface Transactional {
	default  <R> R transactional(EntityManager em, Function<EntityManager, R> function) {
		em.getTransaction().begin();
		R r = function.apply(em);
		em.getTransaction().commit();
		return r;
	}
}
