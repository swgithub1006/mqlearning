package org.coffee.netty.codec;

import org.coffee.netty.protocol.Echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author coffee
 *
 */
public class EchoEncoder extends MessageToByteEncoder<Echo> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Echo msg, ByteBuf out) throws Exception {
//		Assert.assertNotNull("消息不能为空", msg);
		out.writeInt(msg.getLength());
		out.writeLong(msg.getRequestId());
		out.writeBytes(msg.getBody());
	}

}
