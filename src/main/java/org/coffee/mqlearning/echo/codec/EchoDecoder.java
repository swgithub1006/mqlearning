package org.coffee.mqlearning.echo.codec;

import org.coffee.mqlearning.echo.protocol.Echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class EchoDecoder extends LengthFieldBasedFrameDecoder {

	public EchoDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ByteBuf buf = null;
		Echo echo = null;
		try {
			buf = (ByteBuf) super.decode(ctx, in);
			if (buf == null) {
				return null;
			}

			int len = buf.readInt();
			long requestId = buf.readLong();
			byte[] dst = new byte[len - 8];
			buf.readBytes(dst);
			String msg = new String(dst, "GBK");

			echo = new Echo(requestId, msg);
		} finally {
			if (buf != null) {
				buf.release();
			}
		}

		return echo;
	}

}
