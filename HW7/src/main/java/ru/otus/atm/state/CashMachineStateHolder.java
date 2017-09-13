package ru.otus.atm.state;

import java.util.Set;

import ru.otus.atm.CashMachine;
import ru.otus.atm.banknote.Banknote;

/**
 * @author e.petrov. Created 08 - 2017.
 */
public class CashMachineStateHolder implements StateHolder<CashMachine> {

	public static StateHolder<CashMachine> newInstance() {
		return new CashMachineStateHolder();
	}

	private CashMachineStateHolder() {
	}

	private State<Set<Banknote>> state;

	@Override
	public void save(CashMachine cashMachine) {
		this.state = new CashMachineState(cashMachine.clear());
		cashMachine.insert(state.getState());
	}

	@Override
	public void restore(CashMachine cashMachine) {
		if (state != null) {
			cashMachine.clear();
			cashMachine.insert(state.getState());
		}
	}
}
