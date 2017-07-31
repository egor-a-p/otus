package ru.otus.gc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author e.petrov. Created 07 - 2017.
 */
public class MemoryLeakJob extends Thread {

	private List<byte[]> list = new ArrayList<>();

	@Override
	public void run() {
		for (int i = 1; i < Integer.MAX_VALUE; i++) {
			list.add(("MemoryLeakJob" + i).getBytes());
			if(i % 5 == 0) {
				int half = list.size() / 2;
				while (list.size() > half) {
					try {
						Thread.sleep(1000);
						list.remove(list.size() - 1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
