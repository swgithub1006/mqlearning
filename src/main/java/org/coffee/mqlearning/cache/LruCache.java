package org.coffee.mqlearning.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * @date 2019年8月29日
 * @param <K> 键
 * @param <V> 值
 * @Description 依葫芦画瓢的LruCache
 * @see org.apache.ibatis.cache.decorators.LruCache
 */
public class LruCache<K, V> implements Storage<K, V> {

	//缓存容量
	protected final int capacity;
	
	//
	protected final Storage<K, V> lowSpeedStorage;

	/**
	 * 基于 LinkedHashMap 实现淘汰机制
	 */
	private Map<K, K> keyMap;
	
	/**
	 * 最老的键，即要被淘汰的
	 */
	private K eldestKey;

	public LruCache(int capacity, Storage<K, V> delegate) {
		this.capacity = capacity;
		this.lowSpeedStorage = delegate;
		keyMap = new LinkedHashMap<K, K>(this.capacity, .75F, true) { //

			/**
			 * 
			 */
			private static final long serialVersionUID = -4476957430545177369L;

			// LinkedHashMap自带的判断是否删除最老的元素方法，默认返回false，即不删除老数据.我们要做的就是重写这个方法，当满足一定条件时删除老数据
			@Override
			protected boolean removeEldestEntry(Map.Entry<K, K> eldest) {
				boolean tooBig = size() > capacity;
				if (tooBig) {
					eldestKey = eldest.getKey();
				}
				return tooBig;
			}
		};
	}

	@Override
	public V get(K key) {
		//刷新keyMap
		this.keyMap.get(key);
		return this.lowSpeedStorage.get(key);
	}

	@Override
	public void put(K key, V value) {
		this.lowSpeedStorage.put(key, value);
		cycleKeyList(key);
	}

	@Override
	public String getId() {
		return this.lowSpeedStorage.getId();
	}

	@Override
	public V removeByKey(K key) {
//		this.keyMap.remove(key);
		return this.lowSpeedStorage.removeByKey(key);
	}

	@Override
	public void clear() {
		this.keyMap.clear();
		this.lowSpeedStorage.clear();
	}

	@Override
	public int getSize() {
		return this.lowSpeedStorage.getSize();
	}

	private void cycleKeyList(K key) {
		// 添加到 keyMap 中
		this.keyMap.put(key,key);
		// 如果超过上限，则从 lowSpeedStorage 中，移除最少使用的那个
		if (this.eldestKey != null) {
			this.lowSpeedStorage.removeByKey(eldestKey);
			this.eldestKey = null; 
		}
	}

}