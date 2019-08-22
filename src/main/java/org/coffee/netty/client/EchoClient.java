package org.coffee.netty.client;

import org.coffee.model.Echo;
import org.coffee.netty.codec.EchoDecoder;
import org.coffee.netty.codec.EchoEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

//@Slf4j
public class EchoClient {

	// private Channel channel;

	// 配置客户端NIO线程组
	private EventLoopGroup group = new NioEventLoopGroup(1);

	private Bootstrap b = new Bootstrap();

	public EchoClient() {
		b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
				.handler(new LoggingHandler(LogLevel.INFO)).handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						// 解码
						ch.pipeline().addLast(new EchoDecoder(1024 * 1024, 0, 4));
						// 编码
						ch.pipeline().addLast(new EchoEncoder());
						ch.pipeline().addLast("handler", new EchoClientHandler());
					}
				});
	}

	public Channel connect(int port, String host) {
		// 发起异步连接操作
		Channel channel = null;
		try {
			ChannelFuture f = b.connect(host, port).sync();
			channel = f.channel();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return channel;
	}

	public void closeChannel(Channel channel) {
		try {
			channel.closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {
		// 优雅退出，释放NIO线程组
		group.shutdownGracefully();
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// int port = 1234;
		int port = 8080;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				// 采用默认值
				e.printStackTrace();
			}
		}

		EchoClient client = new EchoClient();
		/*
		Channel channel = client.connect(port, "127.0.0.1");
		for (long i = 0; i < 100000; i++) {
			Echo echo = new Echo("吃了没，您呐？");
			if(i==0) {
				echo.setStartTimeStamp(System.currentTimeMillis());
			}
			channel.writeAndFlush(echo);
		}
		*/
		Channel c1 = client.connect(port, "127.0.0.1");
		Channel c2 = client.connect(port, "127.0.0.1");
		Channel c3 = client.connect(port, "127.0.0.1");
		Channel c4 = client.connect(port, "127.0.0.1");
		Channel c5 = client.connect(port, "127.0.0.1");
		Channel c6 = client.connect(port, "127.0.0.1");
		Channel c7 = client.connect(port, "127.0.0.1");
		Channel c8 = client.connect(port, "127.0.0.1");
//		Channel c9 = client.connect(port, "127.0.0.1");
		
//		Thread.sleep(60000);
		
		for (long i = 0; i < 125000; i++) {
			Echo echo = new Echo("吃了没，您呐？");
			if(i==0) {
				echo.setStartTimeStamp(System.currentTimeMillis());
			}
			c1.writeAndFlush(echo);
			c2.writeAndFlush(echo);
			c3.writeAndFlush(echo);
			c4.writeAndFlush(echo);
			c5.writeAndFlush(echo);
			c6.writeAndFlush(echo);
			c7.writeAndFlush(echo);
			c8.writeAndFlush(echo);
//			c9.writeAndFlush(echo);
		}
		
		// channel.writeAndFlush(new Echo("吃了没，您呐？"));
	}
}
