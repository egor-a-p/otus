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

	public static UserDBService getInstance() {
		return new DBServiceHibernateImpl();
	}

	private final EntityManagerFactory factory;

	private DBServiceHibernateImpl() {
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
		log.debug("getLocalStatus");
		return transactional(em(), em -> em.unwrap(Session.class).getTransaction().getStatus().name());
	}

	@Override
	public void save(UserDataSet dataSet) {
		log.debug("save: {}", dataSet);
		dao().save(dataSet);
	}

	@Override
	public UserDataSet read(long id) {
		log.debug("read by id: {}", id);
		return dao().read(id);
	}

	@Override
	public List<UserDataSet> readAll() {
		log.debug("readAll");
		return dao().readAll();
	}

	@Override
	public void shutdown() {
		log.debug("shutdown");
		factory.close();
	}

	@Override
	public UserDataSet readByName(String name) {
		log.debug("readByName: {}", name);
		return dao().readByName(name);
	}
}
