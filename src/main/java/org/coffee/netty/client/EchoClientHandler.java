package org.coffee.netty.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.coffee.model.Echo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EchoClientHandler extends SimpleChannelInboundHandler<Echo> {

	private ConcurrentHashMap<String, String> reqDict = new ConcurrentHashMap<>();
	
	private AtomicLong total = new AtomicLong(0);

	public EchoClientHandler() {
		reqDict.put("刚吃。您这，嘛去？", "嗨，没事溜溜弯儿。");
		reqDict.put("有空，家里坐坐啊。", "回头去给老太太请安。");
	}

	public void channelActive(ChannelHandlerContext ctx) {
//		log.info("client side channel active");
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Echo resp) throws Exception {
		if (resp != null) {
//			log.info("收到服务端的响应:[{}]", resp);
			String msg = resp.getMessage();
			if (reqDict.containsKey(msg)) {
				Echo req = new Echo(reqDict.get(msg));
				ctx.writeAndFlush(req);
			} else if ("end".equals(msg)) {
//				log.info("一次会话结束.共[{}]次会话.",total.incrementAndGet());
				total.incrementAndGet();
				if (Long.valueOf(1000000).equals(total.get()*8)) {
					log.info("共耗时:[{}]秒", (System.currentTimeMillis() - resp.getStartTimeStamp())/1000);
					return ;
				}
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

}
