package ru.otus.dao;

import java.util.List;

import javax.persistence.EntityManager;

import ru.otus.entity.UserEntity;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public class UserDataSetDAOHibernateImpl extends AbstractDataSetHibernateDAO<UserEntity> implements UserDataSetDAO {

	public UserDataSetDAOHibernateImpl(EntityManager em) {
		super(em, UserEntity.class);
	}

	@Override
	public UserEntity readByName(String name) {
		return em.createNamedQuery("UserDataSet.readByName", entityClass)
				 .setParameter("name", name)
				 .getSingleResult();
	}

	@Override
	public List<UserEntity> readAll() {
		return em.createNamedQuery("UserDataSet.readAll", entityClass)
                 .getResultList();
	}
}
