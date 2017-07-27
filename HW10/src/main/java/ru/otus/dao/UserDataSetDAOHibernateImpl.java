package ru.otus.dao;

import java.util.List;

import javax.persistence.EntityManager;

import ru.otus.entity.UserDataSet;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public class UserDataSetDAOHibernateImpl extends AbstractDataSetHibernateDAO<UserDataSet> implements UserDataSetDAO {

	public UserDataSetDAOHibernateImpl(EntityManager em) {
		super(em, UserDataSet.class);
	}

	@Override
	public UserDataSet readByName(String name) {
		return em.createNamedQuery("UserDataSet.readByName", entityClass)
				 .setParameter("name", name)
				 .getSingleResult();
	}

	@Override
	public List<UserDataSet> readAll() {
		return em.createNamedQuery("UserDataSet.readAll", entityClass)
                 .getResultList();
	}
}
