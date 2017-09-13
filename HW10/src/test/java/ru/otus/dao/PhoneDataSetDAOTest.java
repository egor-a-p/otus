package ru.otus.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ru.otus.entity.PhoneEntity;
import ru.otus.persistence.PersistenceUnit;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public class PhoneDataSetDAOTest extends AbstractDAOTest<PhoneEntity> {

	private PhoneDataSetDAO phoneDataSetDAO = new PhoneDataSetHibernateImpl(PersistenceUnit.createEntityManager());

	@Override
	protected PhoneEntity create() {
		PhoneEntity phone = new PhoneEntity();
		phone.setCode(testName.hashCode() % 1000);
		phone.setNumber(phone.getCode() + "" + testName.hashCode());
		return phone;
	}

	@Override
	protected PhoneDataSetDAO dao() {
		return phoneDataSetDAO;
	}

	@Override
	protected void update(PhoneEntity dataSet) {
		dataSet.setNumber("updated " + dataSet.getNumber());
	}

	@Test
	public void shouldLoadByCodeEntity() {
		//given
		PhoneEntity dataSet = create();

		//when
		dataSet = dao().save(dataSet);
		List<PhoneEntity> loaded = dao().readByCode(dataSet.getCode());

		//then
		Assert.assertTrue(loaded.contains(dataSet));
	}
}
