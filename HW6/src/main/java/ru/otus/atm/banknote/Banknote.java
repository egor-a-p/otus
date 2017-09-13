package ru.otus.atm.banknote;

import java.time.Year;

import lombok.Builder;
import lombok.Value;

/**
 * @author e.petrov. Created 08 - 2017.
 */
@Value
@Builder
public class Banknote {
	private final Denomination denomination;
	private final Year releaseYear;
	private final String serialNumber;
}
