package ru.otus.executor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import ru.otus.entity.User;
import ru.otus.orm.ORMException;
import ru.otus.orm.SimpleExecutor;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public class ExecutorTest {

	private static Executor executor;

	private User user;

	@BeforeClass
	public static void beforeAll() {
		executor = SimpleExecutor.getInstance();
	}

	@Rule
	public TestName testName = new TestName();

	@Before
	public void before() {
		//given
		user = new User();
		user.setAge(23);
		user.setName("test user for: " + testName.getMethodName());
		executor.save(user);
	}

	@Test
	public void shouldSaveNewEntityAndLoad() {
		//when
		User loaded = executor.load(User.class, user.getId());

		//then
		Assert.assertNotNull(user.getId());
		Assert.assertEquals(user, loaded);
	}

	@Test
	public void shouldUpdateEntityAndLoad() {
		//when
		user.setName(user.getName() + " updated");
		user.setAge(123);
		executor.save(user);

		//then
		User loaded = executor.load(User.class, user.getId());
		Assert.assertNotNull(user.getId());
		Assert.assertEquals(user, loaded);
	}

	@Test
	public void shouldNotLoadNotSavedUser() {
		//when
		Long id = user.getId() + 1000;

		//then
		User loaded = executor.load(User.class, id);
		Assert.assertNull(loaded);
	}

	@Test(/*then*/expected = ORMException.class)
	public void shouldNotSaveWithId() {
		//when
		user.setId(user.getId() + 1000);
		executor.save(user);
	}

	@Test(/*then*/expected = NullPointerException.class)
	public void shouldNotSaveNull() {
		//when
		executor.save(null);
	}


	@Test(/*then*/expected = NullPointerException.class)
	public void shouldNotLoadNull() {
		//when
		executor.load(User.class, null);
	}
}
