package ru.otus.dao;

import ru.otus.entity.AddressEntity;
import ru.otus.persistence.PersistenceUnit;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public class AddressDataSetDAOTest extends AbstractDAOTest<AddressEntity> {

	private AddressDataSetDAO addressDataSetDAO = new AddressDataSetDAOHibernateImpl(PersistenceUnit.createEntityManager());

	@Override
	protected AddressEntity create() {
		AddressEntity address = new AddressEntity();
		address.setStreet("street for test: " + testName.getMethodName());
		address.setIndex(testName.getMethodName().length());

		return address;
	}

	@Override
	protected AddressDataSetDAO dao() {
		return addressDataSetDAO;
	}

	@Override
	protected void update(AddressEntity dataSet) {
		dataSet.setStreet("updated street for test: " + testName.getMethodName());
	}
}
