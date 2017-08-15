package ru.otus.web;

import ru.otus.persistence.PersistenceUnit;

/**
 * @author e.petrov. Created 08 - 2017.
 */
public class WebApp {
	private final static int PORT = 8080;
	private final static String PUBLIC_HTML = "webapp";

	public static void main(String[] args) {
		PersistenceUnit.initialize();

		PersistenceUnit.destroy();
	}
}
