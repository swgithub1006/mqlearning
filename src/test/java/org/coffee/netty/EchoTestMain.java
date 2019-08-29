package org.coffee.netty;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.coffee.netty.client.EchoClient;
import org.coffee.netty.constant.Constant;
import org.coffee.netty.protocol.Echo;

import io.netty.channel.Channel;

public class EchoTestMain {

	static final EchoClient client = new EchoClient();

	static final ExecutorService pool = Executors.newFixedThreadPool(2);

	static void meeting1() {
		Channel c1 = client.connect(Constant.PORT, "127.0.0.1");
		Echo echo = new Echo(0, "吃了没，您呐？");
		echo.setStartTimeStamp(System.currentTimeMillis());

		Runnable r = new Runnable() {
			@Override
			public void run() {
				for (long i = 0; i < Constant.MEETING_1 / 2; i++) {
					c1.writeAndFlush(echo);
				}
			}
		};

		for (int i = 0; i < 2; i++) {
			pool.submit(r);
		}

	}

	static void meeting2() {
		Channel c1 = client.connect(Constant.PORT, "127.0.0.1");
		Echo echo = new Echo(0, "吃了没，您呐？");
		echo.setStartTimeStamp(System.currentTimeMillis());

		Runnable r = new Runnable() {
			@Override
			public void run() {
				for (long i = 0; i < Constant.MEETING_2 / 2; i++) {
					c1.writeAndFlush(echo);
				}
			}
		};

		for (int i = 0; i < 2; i++) {
			pool.submit(r);
		}
	}

	static void meeting3() {
		Channel c1 = client.connect(Constant.PORT, "127.0.0.1");
		Echo echo = new Echo(0, "吃了没，您呐？");
		echo.setStartTimeStamp(System.currentTimeMillis());

		Runnable r = new Runnable() {
			@Override
			public void run() {
				for (long i = 0; i < Constant.MEETING_3 / 2; i++) {
					c1.writeAndFlush(echo);
				}
			}
		};

		for (int i = 0; i < 2; i++) {
			pool.submit(r);
		}

	}

	public static void main(String[] args) {
//		meeting1();
		meeting2();
//		meeting3();
	}

}
