package org.coffee.mqlearning;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.coffee.mqlearning.threadsafe.TransferService;
import org.junit.Test;

public class TransferServiceTest {

	private static final int THREAD_NUM = 2;

	private static CountDownLatch latch = new CountDownLatch(THREAD_NUM);

	private ExecutorService pool = Executors.newFixedThreadPool(THREAD_NUM);

	private TransferService tfs = new TransferService();

	private int count1w = 10000;

	private int count10w = 100000;

	private int count100w = 1000000;

	void calTime(int count, long end, long start) {
		System.out.format("执行转账%d次,时间:%d毫秒\n", count, (end - start));
	}

	/**
	 * 执行转账10000次,时间:94毫秒
	 * 转账完成,账户余额10000
	 */
	@Test
	public void transferWithLock1w() {
		int count = count1w;
		long start = System.currentTimeMillis();

		for (int j = 0; j < THREAD_NUM; j++) {
			pool.submit(() -> {
				for (int i = 0; i < count / THREAD_NUM; i++) {
					tfs.transferWithLock(1);
				}

				latch.countDown();
			});
		}

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		calTime(count, end, start);
		System.out.format("转账完成,账户余额%d\n", tfs.getBalance());
		pool.shutdown();
	}

	@Test
	public void transferWithLock10w() {
		int count = count10w;
		long start = System.currentTimeMillis();

		for (int j = 0; j < THREAD_NUM; j++) {
			pool.submit(() -> {
				for (int i = 0; i < count / THREAD_NUM; i++) {
					tfs.transferWithLock(1);
				}

				latch.countDown();
			});
		}

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		calTime(count, end, start);
		System.out.format("转账完成,账户余额%d\n", tfs.getBalance());

		pool.shutdown();
	}

	/**
	 * 执行转账1000000次,时间:168毫秒 转账完成,账户余额1000000
	 * 
	 */
	@Test
	public void transferWithLock100w() {
		int count = count100w;
		long start = System.currentTimeMillis();

		for (int j = 0; j < THREAD_NUM; j++) {
			pool.submit(() -> {
				for (int i = 0; i < count / THREAD_NUM; i++) {
					tfs.transferWithLock(1);
				}

				latch.countDown();
			});
		}

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		calTime(count, end, start);
		System.out.format("转账完成,账户余额%d\n", tfs.getBalance());
		pool.shutdown();
	}

	/**
	 * 执行转账10000次,时间:94毫秒 转账完成,账户余额10000
	 * 
	 */
	@Test
	public void transferCAS1w() {
		int count = count1w;
		long start = System.currentTimeMillis();

		for (int j = 0; j < THREAD_NUM; j++) {
			pool.submit(() -> {
				for (int i = 0; i < count / THREAD_NUM; i++) {
					tfs.transferCAS(1);
				}
				
				latch.countDown();
			});
		}

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		calTime(count, end, start);
		System.out.format("转账完成,账户余额%d\n", tfs.getBalance());
		
		pool.shutdown();
	}

	/**
	 * 执行转账100000次,时间:104毫秒 转账完成,账户余额100000
	 * 
	 */
	@Test
	public void transferCAS10w() {
		int count = count10w;
		long start = System.currentTimeMillis();
		
		for (int j = 0; j < THREAD_NUM; j++) {
			pool.submit(() -> {
				for (int i = 0; i < count / THREAD_NUM; i++) {
					tfs.transferCAS(1);
				}
				
				latch.countDown();
			});
		}

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		calTime(count, end, start);
		System.out.format("转账完成,账户余额%d\n", tfs.getBalance());
		pool.shutdown();
	}

	/**
	 * 执行转账1000000次,时间:130毫秒 转账完成,账户余额1000000
	 * 
	 */
	@Test
	public void transferCAS100w() {
		int count = count100w;
		long start = System.currentTimeMillis();
		
		for (int j = 0; j < THREAD_NUM; j++) {
			pool.submit(() -> {
				for (int i = 0; i < count / THREAD_NUM; i++) {
					tfs.transferCAS(1);
				}
				
				latch.countDown();
			});
		}

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		calTime(count, end, start);
		System.out.format("转账完成,账户余额%d\n", tfs.getBalance());
		pool.shutdown();
	}

	/**
	 * 执行转账10000次,时间:97毫秒 转账完成,账户余额10000
	 * 
	 */
	@Test
	public void transferFAA1w() {
		int count = count1w;
		long start = System.currentTimeMillis();
		
		for (int j = 0; j < THREAD_NUM; j++) {
			pool.submit(() -> {
				for (int i = 0; i < count / THREAD_NUM; i++) {
					tfs.transferFAA(1);
				}

				latch.countDown();
			});
		}

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		calTime(count, end, start);
		System.out.format("转账完成,账户余额%d\n", tfs.getBalance());
		pool.shutdown();
	}

	/**
	 * 执行转账100000次,时间:99毫秒 转账完成,账户余额100000
	 * 
	 */
	@Test
	public void transferFAA10w() {
		int count = count10w;
		long start = System.currentTimeMillis();
		
		for (int j = 0; j < THREAD_NUM; j++) {
			pool.submit(() -> {
				for (int i = 0; i < count / THREAD_NUM; i++) {
					tfs.transferFAA(1);
				}

				latch.countDown();
			});
		}

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		calTime(count, end, start);
		System.out.format("转账完成,账户余额%d\n", tfs.getBalance());
		pool.shutdown();
	}

	/**
	 * 执行转账1000000次,时间:123毫秒 转账完成,账户余额1000000
	 * 
	 */
	@Test
	public void transferFAA100w() {
		int count = count100w;
		long start = System.currentTimeMillis();
		
		for (int j = 0; j < THREAD_NUM; j++) {
			pool.submit(() -> {
				for (int i = 0; i < count / THREAD_NUM; i++) {
					tfs.transferFAA(1);
				}

				latch.countDown();
			});
		}

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		calTime(count, end, start);
		System.out.format("转账完成,账户余额%d\n", tfs.getBalance());
		pool.shutdown();
	}

}
