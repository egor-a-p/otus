package ru.otus.atm.department;

import ru.otus.atm.CashMachine;

/**
 * @author e.petrov. Created 08 - 2017.
 */
public interface ATMDepartment extends Iterable<CashMachine> {

	void register(CashMachine... cashMachines);

	void restore();

	int balance();

	static ATMDepartment newInstance() {
		return new ATMDepartmentImpl();
	}
}
