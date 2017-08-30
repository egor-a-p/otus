package ru.otus.atm;

import java.time.Year;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.otus.atm.banknote.Banknote;
import ru.otus.atm.banknote.Denomination;

/**
 * @author e.petrov. Created 08 - 2017.
 */
public class CashMachineTest {
	private CashMachine cashMachine = CashMachine.newInstance();

	@Before
	public void setUp() {
		cashMachine.clear();
		Set<Banknote> banknotesToInsert = new HashSet<>();
		Arrays.stream(Denomination.values()).forEach(d -> IntStream.range(0, 4 + (int) (1000 * Math.random())).forEach(i -> banknotesToInsert
			.add(Banknote.builder().denomination(d).releaseYear(Year.now()).serialNumber(UUID.randomUUID().toString()).build())));
		cashMachine.insert(banknotesToInsert);
	}

	@Test
	public void insertTest() {
		//given
		int balance = cashMachine.balance();
		Set<Banknote> banknotesToInsert = new HashSet<>();
		AtomicInteger amount = new AtomicInteger();
		Arrays.stream(Denomination.values()).forEach(d -> IntStream.range(0, 10).forEach(i -> {
			banknotesToInsert.add(Banknote.builder().denomination(d).releaseYear(Year.now()).serialNumber(UUID.randomUUID().toString()).build());
			amount.addAndGet(d.amount);
		}));

		//when
		cashMachine.insert(banknotesToInsert);

		//then
		Assert.assertEquals(balance + amount.get(), cashMachine.balance());
	}

	@Test
	public void withdrawOptimalTest() throws WithdrawException {
		//given
		int balance = cashMachine.balance();
		int amount = 14750;

		//when
		Set<Banknote> withdraw = cashMachine.withdraw(amount);

		//then
		Assert.assertTrue(withdraw.stream().filter(b -> b.getDenomination() == Denomination.FIFTY).count() == 1);
		Assert.assertTrue(withdraw.stream().filter(b -> b.getDenomination() == Denomination.HUNDRED).count() == 2);
		Assert.assertTrue(withdraw.stream().filter(b -> b.getDenomination() == Denomination.FIVE_HUNDRED).count() == 1);
		Assert.assertTrue(withdraw.stream().filter(b -> b.getDenomination() == Denomination.ONE_THOUSAND).count() == 4);
		Assert.assertTrue(withdraw.stream().filter(b -> b.getDenomination() == Denomination.FIVE_THOUSAND).count() == 2);

		Assert.assertEquals(balance - amount, cashMachine.balance());
	}

	@Test(expected = WithdrawException.class)
	public void withdrawTest() throws WithdrawException {
		while (true) {
			//given
			int balance = cashMachine.balance();
			int amount = ((int) (50000 * Math.random()) % 50) * 50;

			//when
			Set<Banknote> withdraw = cashMachine.withdraw(amount);

			//then
			Assert.assertEquals(balance - amount, cashMachine.balance());
			Assert.assertEquals(amount, sum(withdraw));
		}
	}

	private static int sum(Set<Banknote> banknotes) {
		return banknotes.stream().mapToInt(b -> b.getDenomination().amount).sum();
	}
}
