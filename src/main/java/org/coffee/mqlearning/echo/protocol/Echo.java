package org.coffee.mqlearning.echo.protocol;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author coffee
 *
 */
// @ToString
public class Echo implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7670741182621285695L;

	private int length;

	private String message;

	private byte[] body;

	private long requestId;

	private static final AtomicLong flowNum = new AtomicLong(0);
	
	private transient static long startTimeStamp ;

	public Echo(String msg) {
		this.requestId = flowNum.incrementAndGet();
		this.message = msg;
		this.body = msg.getBytes(Charset.forName("GBK"));
		this.length = body.length + 8;
	}

	public Echo(long requestId, String msg) {
		this.requestId = requestId;
		this.message = msg;
		this.body = msg.getBytes(Charset.forName("GBK"));
		this.length = body.length + 8;
	}

	public int getLength() {
		return length;
	}
	
	public void setStartTimeStamp(long ts) {
		startTimeStamp=ts;
	}
	
	public long getStartTimeStamp() {
		return startTimeStamp;
	}
	

	/*
	 * public void setMessage(String msg) { 
	 * 	this.message = msg; 
	 *  this.body = msg.getBytes(Charset.forName("GBK")); 
	 *  this.length = body.length; 
	 * }
	 */

	public String getMessage() {
		return this.message;
	}

	public long getRequestId() {
		return this.requestId;
	}

	public byte[] getBody() {
		return this.body;
	}

	@Override
	public String toString() {
		return String.format("echo:[message=%s,requestId=%d]", message, requestId);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
