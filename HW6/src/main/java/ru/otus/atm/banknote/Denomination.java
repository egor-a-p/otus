package ru.otus.atm.banknote;

/**
 * @author e.petrov. Created 08 - 2017.
 */
public enum Denomination {
	FIFTY(50),
	HUNDRED(100),
	FIVE_HUNDRED(500),
	ONE_THOUSAND(1000),
	FIVE_THOUSAND(5000);

	public final int amount;

	Denomination(int amount) {
		this.amount = amount;
	}
}
