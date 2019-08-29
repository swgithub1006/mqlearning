package org.coffee.netty.server;

import java.util.concurrent.ConcurrentHashMap;

import org.coffee.netty.protocol.Echo;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class EchoServerTwoWayHander extends ChannelInboundHandlerAdapter {

	private volatile Channel channel = null;

	private ConcurrentHashMap<String, String> respDict = new ConcurrentHashMap<>();

	public EchoServerTwoWayHander() {
		respDict.put("吃了没，您呐？", "您这，嘛去？");
		respDict.put("嗨，没事溜溜弯儿。", "有空，家里坐坐啊。");
		respDict.put("回头去给老太太请安。", "end");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
		Echo req = (Echo) obj;
		String msg = req.getMessage();
		// log.info("收到客户端请求：[{}]", req);
		if (respDict.containsKey(msg)) {
			Echo resp = new Echo(req.getRequestId(), respDict.get(msg));
			ctx.writeAndFlush(resp);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();// 发生异常，关闭链路
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
		channel = ctx.channel();
	}

	public void send(long count) {
		String[] msgs = { "刚吃", "您这，嘛去？", "有空，家里坐坐啊。" };
		for (long i = 0; i < count; i++) {
			for (String msg : msgs) {
				this.channel.writeAndFlush(new Echo(msg));
			}
		}
	}

}
