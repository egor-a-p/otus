package ru.otus.emulator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ru.otus.entity.AddressEntity;
import ru.otus.entity.PhoneEntity;
import ru.otus.entity.UserEntity;
import ru.otus.service.UserService;

/**
 * @author e.petrov. Created 09 - 2017.
 */
public class WorkEmulator extends Thread {
	private final UserService userService;

	public WorkEmulator(UserService userService) {
		this.userService = userService;
		setDaemon(true);
	}

	public static UserEntity create(String threadName, int j) {
		UserEntity user = new UserEntity();
		AddressEntity address = new AddressEntity();

		address.setStreet("street for thread: " + threadName + j);
		address.setIndex(threadName.length());
		user.setUserAddress(address);

		List<PhoneEntity> phones = new ArrayList<>();

		IntStream.range(0, threadName.hashCode() % 3).forEach(i -> {
			PhoneEntity phone = new PhoneEntity();
			phone.setCode(901 + i);
			phone.setNumber("" + threadName.hashCode() % 10000000 + i);
			phone.setUser(user);
			phones.add(phone);
		});

		user.setName(threadName + UUID.randomUUID());
		user.setPassword(UUID.randomUUID().toString());
		user.setUserAddress(address);
		user.setPhones(phones);

		return user;
	}

	@Override
	public void run() {
		String threadName = Thread.currentThread().getName();
		for (int j = 0; j < Integer.MAX_VALUE; j+=100) {
			List<UserEntity> saved = IntStream.range(j, j + (int) (Math.random() * 100))
				.boxed()
				.map(i -> userService.save(create(threadName, i)))
				.collect(Collectors.toList());
			saved.forEach(e -> {
				userService.load(e.getId());
				sleep(1000);
				userService.load(e.getId());
			});
			saved.clear();
			System.gc();
			sleep(3000);
		}
	}

	private void sleep(int factor) {
		try {
			sleep((long) (Math.random() * factor));
		} catch (InterruptedException e) {
		}
	}
}
