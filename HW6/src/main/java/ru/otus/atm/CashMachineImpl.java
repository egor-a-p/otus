package ru.otus.atm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import ru.otus.atm.banknote.Banknote;
import ru.otus.atm.banknote.Denomination;

/**
 * @author e.petrov. Created 08 - 2017.
 */
@EqualsAndHashCode(exclude = {"balance", "cashStorage"})
class CashMachineImpl implements CashMachine {
	private final Map<Denomination, Set<Banknote>> cashStorage;
	private int balance;
	private final UUID uuid;

	CashMachineImpl() {
		this.cashStorage = new TreeMap<>((d1, d2) -> Integer.compare(d2.amount, d1.amount));
		this.uuid = UUID.randomUUID();
	}

	private static Set<Banknote> asSet(Banknote... banknotes) {
		return new HashSet<>(Arrays.asList(banknotes));
	}

	@Override
	public void insert(Set<Banknote> banknotes) {
		banknotes.forEach(b -> {
			Objects.requireNonNull(b, "Banknote is null!");
			balance += b.getDenomination().amount;
			cashStorage.merge(b.getDenomination(), asSet(b), (v1, v2) -> {
				v1.addAll(v2);
				return v1;
			});
		});
	}

	@Override
	public Set<Banknote> withdraw(int amount) throws WithdrawException {
		Set<Banknote> result = new HashSet<>();
		int currentAmount = amount;
		for (Map.Entry<Denomination, Set<Banknote>> entry : cashStorage.entrySet()) {
			int count = currentAmount / entry.getKey().amount;
			if (count != 0) {
				Iterator<Banknote> iterator = entry.getValue().iterator();
				while (iterator.hasNext() && count-- > 0) {
					result.add(iterator.next());
					iterator.remove();
					currentAmount -= entry.getKey().amount;
				}
			}
		}
		if (currentAmount != 0) {
			insert(result);
			throw new WithdrawException("It is impossible to withdraw this amount!");
		} else {
			balance -= amount;
			return result;
		}
	}

	@Override
	public int balance() {
		return balance;
	}

	@Override
	public Set<Banknote> clear() {
		Set<Banknote> banknotes = cashStorage.entrySet().stream().flatMap(e -> e.getValue().stream()).collect(Collectors.toSet());
		cashStorage.clear();
		balance = 0;
		return banknotes;
	}
}
