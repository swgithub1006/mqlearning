package org.coffee.mqlearning.threadsafe;

import java.util.concurrent.atomic.AtomicInteger;

public class TransferService {

	private AtomicInteger balanceAtomic = new AtomicInteger(0);

	/**
	 * 通过synchronized实现的线程安全的转账
	 * @param amount
	 */
	public synchronized void transferWithLock(int amount) {
		int current = balanceAtomic.get();
		int value = current + amount;
		balanceAtomic.set(value);
	}

	/**
	 * 通过CAS实现的线程安全的转账
	 * @param amount
	 */
	public void transferCAS(int amount) {
/*		if(amount<=0) {
			throw new IllegalArgumentException("参数错误");
		}*/
		while (true) {
			int current = balanceAtomic.get();
			int value = current + amount;
			if (value != current) {
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

	/**
	 * 通过FAA实现的线程安全的转账 FAA 原语(Fetch and Add)
	 * @param amount
	 */
	public void transferFAA(int amount) {
		balanceAtomic.getAndAdd(amount);
	}

	public int getBalance() {
		return balanceAtomic.get();
	}

}
