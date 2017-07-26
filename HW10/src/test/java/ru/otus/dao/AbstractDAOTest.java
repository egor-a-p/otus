package ru.otus.dao;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import ru.otus.entity.DataSet;
import ru.otus.persistence.PersistenceUnit;

/**
 * Created by egor on 25.07.17.
 */
public abstract class AbstractDAOTest<T extends DataSet> {

	@BeforeClass
	public static void initialize() {
		PersistenceUnit.initialize();
	}

	@AfterClass
	public static void destroy() {
		PersistenceUnit.destroy();
	}

	@Rule
	public TestName testName = new TestName();

	protected abstract T create();

	protected abstract DataSetDAO<T> dao();

	@Test
	public void shouldSaveEntity() {
		//given
		T dataSet = create();

		//when
		dao().save(dataSet);

		//then
		Assert.assertNotNull(dataSet.getId());
	}

	@Test
	public void shouldLoadEntity() {
		//given
		T dataSet = create();

		//when
		dao().save(dataSet);
		T loaded = dao().read(dataSet.getId());

		//then
		Assert.assertEquals(dataSet, loaded);
	}

	@Test
	public void shouldLoadAllSavedEntities() {
		//given
		T dataSet = create();

		//when
		dao().save(dataSet);

		//then
		Assert.assertTrue(dao().readAll().contains(dataSet));
	}
}

