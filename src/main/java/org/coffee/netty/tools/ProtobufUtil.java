package org.coffee.netty.tools;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
/**
 * 
 * @author coffee
 * protobuf工具类<br/>
 * io.netty.handler.codec.protobuf.ProtobufDecoder
 */
public class ProtobufUtil {

	private static final boolean HAS_PARSER;

	static {
		boolean hasParser = false;
		try {
			// MessageLite.getParsetForType() is not available until protobuf
			// 2.5.0.
			MessageLite.class.getDeclaredMethod("getParserForType");
			hasParser = true;
		} catch (Throwable t) {
			t.printStackTrace();
		}

		HAS_PARSER = hasParser;
	}

	private final MessageLite prototype;

	private final ExtensionRegistry extensionRegistry;

	public ProtobufUtil(MessageLite prototype) {
		this(prototype, null);
	}

	public ProtobufUtil(MessageLite prototype,ExtensionRegistry extensionRegistry) {
		if (prototype == null) {
			throw new NullPointerException("prototype");
		}
		this.prototype = prototype.getDefaultInstanceForType();
		this.extensionRegistry = extensionRegistry;
	}

	/**
	 * 将byte [] 转换成MessageLite
	 * @param array byte [] 
	 * @return MessageLite
	 * @throws InvalidProtocolBufferException
	 */
	public MessageLite generateMsgFromBytes(byte[] array)
			throws InvalidProtocolBufferException {
		MessageLite msg;
		if (extensionRegistry == null) {
			if (HAS_PARSER) {
				msg = prototype.getParserForType().parseFrom(array);
			} else {
				msg = prototype.newBuilderForType().mergeFrom(array).build();
			}
		} else {
			if (HAS_PARSER) {
				msg = prototype.getParserForType().parseFrom(array,
						extensionRegistry);
			} else {
				msg = prototype.newBuilderForType()
						.mergeFrom(array, extensionRegistry).build();
			}
		}
		return msg;
	}

}
