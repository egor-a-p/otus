package ru.otus.persistence;

import java.util.Objects;
import java.util.function.Function;

import javax.persistence.EntityManager;

import org.hibernate.Session;

import ru.otus.dao.DataSetDAO;
import ru.otus.entity.DataSet;

/**
 * Created by egor on 25.07.17.
 */
public abstract class Manageable<T extends DataSet> implements DataSetDAO<T> {

	protected final EntityManager em;
	protected final Class<T> entityClass;

	/**
	 * Тот самый неловкий момент, когда нет DI...
	 */
	protected Manageable(EntityManager em, Class<T> entityClass) {
		Objects.requireNonNull(em);
		Objects.requireNonNull(entityClass);
		this.em = em;
		this.entityClass = entityClass;
	}

	public void save(T dataSet) {
		 transactional(em -> {
			 dataSet.setId(em.merge(dataSet).getId());
			 return dataSet;
		 });
	}

	public T read(long id){
		return em.find(entityClass, id);
	}

	public void close(){
		em.close();
	}

	public String status() {
		return transactional(em -> em.unwrap(Session.class).getTransaction().getStatus().name());
	}

	protected <R> R transactional(Function<EntityManager, R> function) {
		em.getTransaction().begin();
		R r = function.apply(em);
		em.getTransaction().commit();
		return r;
	}
}
