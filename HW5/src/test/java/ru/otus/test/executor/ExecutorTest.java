package ru.otus.test.executor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ru.otus.test.TestExecutor;
import ru.otus.test.engine.TestResult;
import ru.otus.test.executor.stub.FullTestStub;
import ru.otus.test.executor.stub.SimpleTestStub;

/**
 * Created by egor on 30.07.17.
 */
public class ExecutorTest {

	@Test
	public void shouldExecuteTestsFromPackage() {
		//given
		String packageName = "ru.otus.test.executor.stub";
		int before = FullTestStub.beforeCounter.get();
		int after = FullTestStub.afterCounter.get();

		//when
		List<TestResult> testResults = TestExecutor.execute(packageName);

		//then
		Assert.assertEquals(before + 3, FullTestStub.beforeCounter.get());
		Assert.assertEquals(after + 2, FullTestStub.afterCounter.get());
		Assert.assertEquals(6, testResults.size());

		Assert.assertEquals(1, testResults.stream().filter(r -> r.getStatus() == TestResult.Status.PASSED &&
			r.getTestClassName().equals(SimpleTestStub.class.getName())).count());
		Assert.assertEquals(1, testResults.stream().filter(r -> r.getStatus() == TestResult.Status.FAILED &&
			r.getTestClassName().equals(SimpleTestStub.class.getName())).count());
		Assert.assertEquals(1, testResults.stream().filter(r -> r.getStatus() == TestResult.Status.ERROR &&
			r.getTestClassName().equals(SimpleTestStub.class.getName())).count());

		Assert.assertEquals(1, testResults.stream().filter(r -> r.getStatus() == TestResult.Status.PASSED &&
			r.getTestClassName().equals(FullTestStub.class.getName())).count());
		Assert.assertEquals(1, testResults.stream().filter(r -> r.getStatus() == TestResult.Status.FAILED &&
			r.getTestClassName().equals(FullTestStub.class.getName())).count());
		Assert.assertEquals(1, testResults.stream().filter(r -> r.getStatus() == TestResult.Status.ERROR &&
			r.getTestClassName().equals(FullTestStub.class.getName())).count());
	}
}
