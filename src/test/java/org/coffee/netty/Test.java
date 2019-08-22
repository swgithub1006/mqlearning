package org.coffee.netty;

import java.io.UnsupportedEncodingException;

public class Test {

	public static void main(String[] args) {
		String str = "你好";
		try {
			System.out.println(str.getBytes("GBK").length);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
