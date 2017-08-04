package ru.otus.atm.department;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ru.otus.atm.CashMachine;
import ru.otus.atm.state.CashMachineStateHolder;
import ru.otus.atm.state.StateHolder;

/**
 * @author e.petrov. Created 08 - 2017.
 */
class ATMDepartmentImpl implements ATMDepartment {

	private final Map<CashMachine, StateHolder<CashMachine>> cashMachines;

	ATMDepartmentImpl() {
		this.cashMachines = new HashMap<>();
	}

	@Override
	public void register(CashMachine... cashMachines) {
		Arrays.stream(cashMachines).forEach(m -> {
			StateHolder<CashMachine> stateHolder = CashMachineStateHolder.newInstance();
			stateHolder.save(m);
			this.cashMachines.put(m, stateHolder);
		});
	}

	@Override
	public Iterator<CashMachine> iterator() {
		return cashMachines.keySet().iterator();
	}

	@Override
	public void restore() {
		cashMachines.entrySet().forEach(e -> e.getValue().restore(e.getKey()));
	}

	@Override
	public int balance() {
		return cashMachines.keySet().stream().mapToInt(CashMachine::balance).sum();
	}
}
