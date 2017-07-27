package ru.otus.dao;

import java.util.List;

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

	protected abstract void update(T dataSet);

	@Test
	public void shouldSaveEntity() {
		//given
		T dataSet = create();

		//when
		dataSet = dao().save(dataSet);

		//then
		Assert.assertNotNull(dataSet.getId());
	}

	@Test
	public void shouldLoadEntity() {
		//given
		T dataSet = create();

		//when
		dataSet = dao().save(dataSet);
		T loaded = dao().read(dataSet.getId());

		//then
		Assert.assertEquals(dataSet, loaded);
	}

	@Test
	public void shouldUpdateEntity() {
		//given
		T dataSet = create();

		//when
		dataSet = dao().save(dataSet);
		update(dataSet);
		DataSet updated = dao().save(dataSet);
		T loaded = dao().read(dataSet.getId());

		//then
		Assert.assertEquals(dataSet.getId(), loaded.getId());
		Assert.assertEquals(updated.getId(), loaded.getId());
	}

	@Test
	public void shouldLoadAllSavedEntities() {
		//given
		T dataSet1 = create();
		T dataSet2 = create();

		//when
		dataSet1 = dao().save(dataSet1);
		dataSet2 = dao().save(dataSet2);

		//then
		List<T> dataSets = dao().readAll();
		Assert.assertNotNull(dataSets);
		Assert.assertTrue(!dataSets.isEmpty());
		Assert.assertTrue(dataSets.contains(dataSet1));
		Assert.assertTrue(dataSets.contains(dataSet2));
	}
}

