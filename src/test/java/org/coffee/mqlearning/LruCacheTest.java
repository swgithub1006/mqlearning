package org.coffee.mqlearning;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruCacheTest {
	
	private static String eldestKey;

	public static void main(String[] args) {
		LinkedHashMap<String, String> lhm = new LinkedHashMap<String, String>(2, 0.75f, true) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 7066210825607985497L;

			@Override
			protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
				boolean tooBig = size() > 2;
				if (tooBig) {
					eldestKey = eldest.getKey();
				}
				return tooBig;
			}
		};
		lhm.put("123", "123");
		lhm.put("345", "345");
		lhm.put("456", "456");
		System.out.println(lhm);
		System.out.println(eldestKey);
	}

}
