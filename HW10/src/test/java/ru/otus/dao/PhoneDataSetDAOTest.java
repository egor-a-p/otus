package ru.otus.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ru.otus.entity.PhoneDataSet;
import ru.otus.persistence.PersistenceUnit;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public class PhoneDataSetDAOTest extends AbstractDAOTest<PhoneDataSet> {

	private PhoneDataSetDAO phoneDataSetDAO = new PhoneDataSetHibernateImpl(PersistenceUnit.createEntityManager());

	@Override
	protected PhoneDataSet create() {
		PhoneDataSet phone = new PhoneDataSet();
		phone.setCode(testName.hashCode() % 1000);
		phone.setNumber(phone.getCode() + "" + testName.hashCode());
		return phone;
	}

	@Override
	protected PhoneDataSetDAO dao() {
		return phoneDataSetDAO;
	}

	@Override
	protected void update(PhoneDataSet dataSet) {
		dataSet.setNumber("updated " + dataSet.getNumber());
	}

	@Test
	public void shouldLoadByCodeEntity() {
		//given
		PhoneDataSet dataSet = create();

		//when
		dataSet = dao().save(dataSet);
		List<PhoneDataSet> loaded = dao().readByCode(dataSet.getCode());

		//then
		Assert.assertTrue(loaded.contains(dataSet));
	}
}
