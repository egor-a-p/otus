package ru.otus.atm.department;

import java.time.Year;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.otus.atm.CashMachine;
import ru.otus.atm.WithdrawException;
import ru.otus.atm.banknote.Banknote;
import ru.otus.atm.banknote.Denomination;

/**
 * @author e.petrov. Created 08 - 2017.
 */
public class ATMDepartmentTest {
	private ATMDepartment department;

	@Before
	public void setUp() {
		department = ATMDepartment.newInstance();
		IntStream.range(0, 10).forEach(i -> {
			CashMachine cashMachine = CashMachine.newInstance();
			Set<Banknote> banknotesToInsert = new HashSet<>();
			Arrays.stream(Denomination.values()).forEach(d -> IntStream.range(0, 4 + (int) (1000 * Math.random())).forEach(j -> banknotesToInsert
				.add(Banknote.builder().denomination(d).releaseYear(Year.now()).serialNumber(UUID.randomUUID().toString()).build())));
			cashMachine.insert(banknotesToInsert);
			department.register(cashMachine);
		});
	}

	@Test
	public void balanceTest() {
		//given
		int balance = 0;

		//when
		for (CashMachine cashMachine : department) {
			balance += cashMachine.balance();
		}

		//then
		Assert.assertEquals(balance, department.balance());
	}

	@Test
	public void registerTest() {
		//given
		CashMachine cashMachine = CashMachine.newInstance();

		//when
		department.register(cashMachine);

		//then
		for (CashMachine machine : department) {
			if (cashMachine.equals(machine)) {
				return;
			}
		}
		Assert.fail();
	}

	@Test
	public void restoreTest() {
		//given
		int balance = department.balance();
		int amount = 14750;

		//when
		department.forEach(c -> {
			try {
				Assert.assertEquals(amount, sum(c.withdraw(amount)));
			} catch (WithdrawException e) {
			}
		});

		department.restore();

		//then
		Assert.assertEquals(balance, department.balance());
	}

	private static int sum(Set<Banknote> banknotes) {
		return banknotes.stream().mapToInt(b -> b.getDenomination().amount).sum();
	}
}
