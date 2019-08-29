package org.coffee.netty;

import org.coffee.netty.client.EchoClient;
import org.coffee.netty.constant.Constant;

public class EchoTest {

	static void meeting10000() {
//		EchoServer es = new EchoServer().bind(Constant.PORT);
//		es.send(Constant.MEETING_1);
		EchoClient client = new EchoClient(Constant.PORT, "127.0.0.1");
		client.send(Constant.MEETING_1);
	}

	public static void main(String[] args) {
		meeting10000();
	}

}
