/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.coffee.netty.server;

import org.coffee.netty.codec.EchoDecoder;
import org.coffee.netty.codec.EchoEncoder;
import org.coffee.netty.constant.Constant;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoServer {

	// 配置服务端的NIO线程组
	private EventLoopGroup bossGroup = new NioEventLoopGroup(2);

	private EventLoopGroup workerGroup = new NioEventLoopGroup(1);

	private ServerBootstrap b = new ServerBootstrap();

	// private EchoServerHandler esh = new EchoServerHandler();
	private EchoServerTwoWayHander esh = new EchoServerTwoWayHander();

	public EchoServer() {
		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100)
				.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) {
						// 解码
						ch.pipeline().addLast(new EchoDecoder(1024 * 1024, 0, 4));
						// 编码
						ch.pipeline().addLast(new EchoEncoder());

						ch.pipeline().addLast(esh);
					}
				});
	}

	public EchoServer bind(int port) {
		try {
			// 绑定端口，同步等待成功
			ChannelFuture f = b.bind(port).sync();
			// 等待服务端监听端口关闭
			f.channel().closeFuture().sync();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this;
	}

	public void send(long count) {
		esh.send(count);
	}

	public void shutdown() {
		// 优雅退出，释放线程池资源
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}

	public static void main(String[] args) throws Exception {
		int port = 8080;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		EchoServer es = new EchoServer().bind(port);
		es.send(Constant.MEETING_2);
	}
}
