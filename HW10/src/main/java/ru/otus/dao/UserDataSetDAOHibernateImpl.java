package ru.otus.dao;

import java.util.List;

import javax.persistence.EntityManager;

import ru.otus.entity.UserDataSet;
import ru.otus.transaction.Transactional;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public class UserDataSetDAOHibernateImpl implements UserDataSetDAO, Transactional {

	private EntityManager em;

	public UserDataSetDAOHibernateImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public void save(UserDataSet dataSet) {
		transactional(em, (em) -> em.merge(dataSet));
	}

	@Override
	public UserDataSet read(long id) {
		return em.find(UserDataSet.class, id);
	}

	@Override
	public UserDataSet readByName(String name) {
		return em.createNamedQuery("UserDataSet.readByName", UserDataSet.class).setParameter("name", name).getSingleResult();
	}

	@Override
	public List<UserDataSet> readAll() {
		return em.createNamedQuery("UserDataSet.readAll", UserDataSet.class).getResultList();
	}
}
