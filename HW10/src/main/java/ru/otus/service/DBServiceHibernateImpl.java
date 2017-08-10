package ru.otus.service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import ru.otus.dao.UserDataSetDAO;
import ru.otus.dao.UserDataSetDAOHibernateImpl;
import ru.otus.entity.UserEntity;
import ru.otus.persistence.PersistenceUnit;

/**
 * @author e.petrov. Created 07 - 2017.
 */
@Slf4j
public class DBServiceHibernateImpl implements UserDBService {

	public static UserDBService getInstance() {
		return new DBServiceHibernateImpl();
	}

	private UserDataSetDAO userDataSetDAO;

	private DBServiceHibernateImpl() {
		this.userDataSetDAO = new UserDataSetDAOHibernateImpl(PersistenceUnit.createEntityManager());
	}

	@Override
	public String getLocalStatus() {
		log.debug("getLocalStatus");
		return userDataSetDAO.status();
	}

	@Override
	public void save(UserEntity dataSet) {
		log.debug("save: {}", dataSet);
		userDataSetDAO.save(dataSet);
	}

	@Override
	public UserEntity read(long id) {
		log.debug("read by id: {}", id);
		return userDataSetDAO.read(id);
	}

	@Override
	public List<UserEntity> readAll() {
		log.debug("readAll");
		return userDataSetDAO.readAll();
	}

	@Override
	public void shutdown() {
		log.debug("shutdown");
		userDataSetDAO.close();
	}

	@Override
	public UserEntity readByName(String name) {
		log.debug("readByName: {}", name);
		return userDataSetDAO.readByName(name);
	}
}
