package ru.otus.dao;

import ru.otus.entity.AddressDataSet;
import ru.otus.persistence.PersistenceUnit;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public class AddressDataSetDAOTest extends AbstractDAOTest<AddressDataSet> {

	private AddressDataSetDAO addressDataSetDAO = new AddressDataSetDAOHibernateImpl(PersistenceUnit.createEntityManager());

	@Override
	protected AddressDataSet create() {
		AddressDataSet address = new AddressDataSet();
		address.setStreet("street for test: " + testName.getMethodName());
		address.setIndex(testName.getMethodName().length());

		return address;
	}

	@Override
	protected AddressDataSetDAO dao() {
		return addressDataSetDAO;
	}

	@Override
	protected void update(AddressDataSet dataSet) {
		dataSet.setStreet("updated street for test: " + testName.getMethodName());
	}
}
