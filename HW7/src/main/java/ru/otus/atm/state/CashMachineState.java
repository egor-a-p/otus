package ru.otus.atm.state;

import java.util.Collections;
import java.util.Set;

import lombok.Getter;
import ru.otus.atm.banknote.Banknote;

/**
 * @author e.petrov. Created 08 - 2017.
 */
class CashMachineState implements State<Set<Banknote>> {
	@Getter
	private final Set<Banknote> state;

	CashMachineState(Set<Banknote> state) {
		this.state = Collections.unmodifiableSet(state);
	}
}
