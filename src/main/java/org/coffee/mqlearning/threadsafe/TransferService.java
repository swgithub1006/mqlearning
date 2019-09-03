package org.coffee.mqlearning.threadsafe;

import java.util.concurrent.atomic.AtomicInteger;

public class TransferService {

	private AtomicInteger balanceAtomic = new AtomicInteger(0);

	public synchronized void transferWithLock(int amount) {
		int current = balanceAtomic.get();
		int value = current + amount;
		balanceAtomic.set(value);
	}

	public void transferCAS(int amount) {
		while (true) {
			int current = balanceAtomic.get();
			int value = current + amount;
			if (value > current) {
				if (balanceAtomic.compareAndSet(current, value)) {
					break;
				} else {
					continue;
				}
			} else {
				break;
			}
		}
	}

	public void transferFAA(int amount) {
		balanceAtomic.addAndGet(amount);
	}

	public int getBalance() {
		return balanceAtomic.get();
	}

}
