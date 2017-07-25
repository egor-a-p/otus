package ru.otus.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;

import lombok.extern.slf4j.Slf4j;
import ru.otus.dao.UserDataSetDAO;
import ru.otus.dao.UserDataSetDAOHibernateImpl;
import ru.otus.entity.UserDataSet;
import ru.otus.transaction.Transactional;

/**
 * @author e.petrov. Created 07 - 2017.
 */
@Slf4j
public class DBServiceHibernateImpl implements UserDBService, Transactional {

	public static final String PERSISTENT_UNIT_NAME = "users";

	private final EntityManagerFactory factory;

	public DBServiceHibernateImpl() {
		this.factory = Persistence.createEntityManagerFactory(PERSISTENT_UNIT_NAME);
	}

	private EntityManager em() {
		return factory.createEntityManager();
	}

	private UserDataSetDAO dao() {
		return new UserDataSetDAOHibernateImpl(em());
	}

	@Override
	public String getLocalStatus() {
		return transactional(em(), em -> em.unwrap(Session.class).getTransaction().getStatus().name());
	}

	@Override
	public void save(UserDataSet dataSet) {
		dao().save(dataSet);
	}

	@Override
	public UserDataSet read(long id) {
		return dao().read(id);
	}

	@Override
	public List<UserDataSet> readAll() {
		return dao().readAll();
	}

	@Override
	public void shutdown() {
		factory.close();
	}

	@Override
	public UserDataSet readByName(String name) {
		return dao().readByName(name);
	}
}
