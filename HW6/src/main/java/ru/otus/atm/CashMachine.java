package ru.otus.atm;

import java.util.Set;

import ru.otus.atm.banknote.Banknote;

/**
 * @author e.petrov. Created 08 - 2017.
 */
public interface CashMachine {

	void insert(Set<Banknote> banknotes);

	Set<Banknote> withdraw(int amount) throws WithdrawException;

	int balance();

	void clear();

	static CashMachine newInstance() {
		return new CashMachineImpl();
	}
}
