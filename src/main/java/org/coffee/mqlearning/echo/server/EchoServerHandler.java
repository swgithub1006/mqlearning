package org.coffee.mqlearning.echo.server;

import java.util.concurrent.ConcurrentHashMap;

import org.coffee.mqlearning.echo.protocol.Echo;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

//@Slf4j
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

	private ConcurrentHashMap<String, String> respDict = new ConcurrentHashMap<>();

	public EchoServerHandler() {
		respDict.put("吃了没，您呐？", "刚吃。您这，嘛去？");
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

	}

}
