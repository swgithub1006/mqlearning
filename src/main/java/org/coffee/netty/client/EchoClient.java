package org.coffee.netty.client;

import org.coffee.netty.codec.EchoDecoder;
import org.coffee.netty.codec.EchoEncoder;
import org.coffee.netty.constant.Constant;

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
	
	private EchoClientTwoWayHandler ect = new EchoClientTwoWayHandler();

	public EchoClient() {
		b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
				.handler(new LoggingHandler(LogLevel.INFO)).handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						// 解码
						ch.pipeline().addLast(new EchoDecoder(1024 * 1024, 0, 4));
						// 编码
						ch.pipeline().addLast(new EchoEncoder());
//						ch.pipeline().addLast("handler", new EchoClientHandler());
						ch.pipeline().addLast("handler", ect);
					}
				});
	}
	
	
	public EchoClient(int port, String host) {
		b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
				.handler(new LoggingHandler(LogLevel.INFO)).handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						// 解码
						ch.pipeline().addLast(new EchoDecoder(1024 * 1024, 0, 4));
						// 编码
						ch.pipeline().addLast(new EchoEncoder());
//						ch.pipeline().addLast("handler", new EchoClientHandler());
						ch.pipeline().addLast("handler", ect);
					}
				});
		try {
			b.connect(host, port).sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
	
	public void send(long count) {
		ect.send(count);
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

		/*		
 		EchoClient client = new EchoClient();

		Channel c1 = client.connect(port, "127.0.0.1");
		for (long i = 0; i < Constant.MEETING_1; i++) {
			Echo echo = new Echo("吃了没，您呐？");
			if (i == 0) {
				echo.setStartTimeStamp(System.currentTimeMillis());
			}
			c1.writeAndFlush(echo);
		}
		*/
		EchoClient client = new EchoClient(port, "127.0.0.1");
		client.send(Constant.MEETING_2);
		
	}
}
