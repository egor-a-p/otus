package ru.otus.service;

import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import ru.otus.entity.AddressDataSet;
import ru.otus.entity.PhoneDataSet;
import ru.otus.entity.UserDataSet;
import ru.otus.persistence.PersistenceUnit;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public class UserDBServiceTest {

	private static UserDBService userService;

	@BeforeClass
	public static void beforeAll() {
		PersistenceUnit.initialize();
		userService = DBServiceHibernateImpl.getInstance();
	}

	@Rule
	public TestName testName = new TestName();

	@Test
	public void shouldSaveUser() {
		AddressDataSet address = new AddressDataSet();
		address.setStreet("test street for: " + testName.getMethodName());
		address.setIndex(testName.hashCode());

		PhoneDataSet phone1 = new PhoneDataSet();
		phone1.setCode(777);
		phone1.setNumber(phone1.getCode() + "" + testName.hashCode());

		PhoneDataSet phone2 = new PhoneDataSet();
		phone2.setCode(888);
		phone2.setNumber(phone2.getCode() + "" + testName.hashCode());

		UserDataSet user = new UserDataSet();
		user.setName("test user for: " + testName.getMethodName());
		user.setAddress(address);
		user.setPhones(Arrays.asList(phone1, phone2));

		userService.save(user);
	}

	@AfterClass
	public static void afterAll() {
		userService.shutdown();
		PersistenceUnit.destroy();
	}

}
