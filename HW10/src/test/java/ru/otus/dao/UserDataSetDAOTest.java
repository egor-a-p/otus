package ru.otus.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import ru.otus.entity.AddressDataSet;
import ru.otus.entity.PhoneDataSet;
import ru.otus.entity.UserDataSet;
import ru.otus.persistence.PersistenceUnit;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public class UserDataSetDAOTest extends AbstractDAOTest {

	private UserDataSetDAO userDataSetDAO = new UserDataSetDAOHibernateImpl(PersistenceUnit.createEntityManager());

	@Override
	protected UserDataSet create() {
		AddressDataSet address = new AddressDataSet();
		address.setStreet("street for test: " + testName.getMethodName());
		address.setIndex(testName.getMethodName().length());

		List<PhoneDataSet> phones = new ArrayList<>();

		IntStream.range(0, testName.hashCode() % 5).forEach(i -> {
			PhoneDataSet phone = new PhoneDataSet();
			phone.setCode(i);
			phone.setNumber("" + testName.hashCode() + i);
			phones.add(phone);
		});


		UserDataSet user = new UserDataSet();
		user.setName("user for test: " + testName.getMethodName() + 1000 * Math.random());
		user.setUserAddress(address);
		user.setPhones(phones);

		return user;
	}

	@Override
	protected UserDataSetDAO dao() {
		return userDataSetDAO;
	}

}
