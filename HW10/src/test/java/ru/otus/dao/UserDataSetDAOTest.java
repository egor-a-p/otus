package ru.otus.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;

import ru.otus.entity.AddressEntity;
import ru.otus.entity.PhoneEntity;
import ru.otus.entity.UserEntity;
import ru.otus.persistence.PersistenceUnit;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public class UserDataSetDAOTest extends AbstractDAOTest<UserEntity> {

	private UserDataSetDAO userDataSetDAO = new UserDataSetDAOHibernateImpl(PersistenceUnit.createEntityManager());

	@Override
	protected UserEntity create() {
		UserEntity user = new UserEntity();
		AddressEntity address = new AddressEntity();
		address.setStreet("street for test: " + testName.getMethodName());
		address.setIndex(testName.getMethodName().length());
		address.setUser(user);

		List<PhoneEntity> phones = new ArrayList<>();

		IntStream.range(0, testName.hashCode() % 5).forEach(i -> {
			PhoneEntity phone = new PhoneEntity();
			phone.setCode(i);
			phone.setNumber("" + testName.hashCode() + i);
			phone.setUser(user);
			phones.add(phone);
		});

		user.setName("user for test: " + testName.getMethodName() + 1000 * Math.random());
		user.setUserAddress(address);
		user.setPhones(phones);

		return user;
	}

	@Override
	protected UserDataSetDAO dao() {
		return userDataSetDAO;
	}

	@Override
	protected void update(UserEntity dataSet) {
		dataSet.setName("updated user for test: " + testName.getMethodName() + 1000 * Math.random());
	}

	@Test
	public void shouldLoadByNameEntity() {
		//given
		UserEntity dataSet = create();

		//when
		dataSet = dao().save(dataSet);
		UserEntity loaded = dao().readByName(dataSet.getName());

		//then
		Assert.assertEquals(dataSet, loaded);
	}
}
