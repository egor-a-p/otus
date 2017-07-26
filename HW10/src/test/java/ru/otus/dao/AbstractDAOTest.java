package ru.otus.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import ru.otus.entity.DataSet;

import static org.hamcrest.Matchers.containsInAnyOrder;

/**
 * Created by egor on 25.07.17.
 */
public abstract class AbstractDAOTest<T extends DataSet> {

	@Rule
	public TestName testName = new TestName();

	protected abstract T create();

	protected abstract DataSetDAO<T> dao();

	protected List<T> saved = new ArrayList<>();

	@Test
	public void shouldSaveEntity() {
		//given
		T dataSet = create();

		//when
		dao().save(dataSet);
		saved.add(dataSet);

		//then
		Assert.assertNotNull(dataSet.getId());
	}

	@Test
	public void shouldLoadEntity() {
		//given
		T dataSet = create();

		//when
		dao().save(dataSet);
		saved.add(dataSet);
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
		saved.add(dataSet);

		//then
		Assert.assertThat(dao().readAll(), containsInAnyOrder(saved.toArray()));
	}
}

