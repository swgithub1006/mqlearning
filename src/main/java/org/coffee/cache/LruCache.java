package org.coffee.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * @author coffee 
 * @date 2019年8月29日
 * @param <K> 键
 * @param <V> 值
 * 依葫芦画瓢的LruCache
 * @see org.apache.ibatis.cache.decorators.LruCache
 */
public class LruCache<K, V> implements Storage<K, V> {

	protected final Storage<K, V> lowSpeedStorage;

	protected final int capacity;

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
		setSize(capacity);
//		int size = (int) Math.ceil(this.capacity / 0.75f) + 1;
//		setSize(size);
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
		// 循环 keyMap
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

	protected void setSize(final int size) {
		// LinkedHashMap的一个构造函数，当参数accessOrder为true时，即会按照访问顺序排序，最近访问的放在最前，最早访问的放在后面
		keyMap = new LinkedHashMap<K, K>(size, .75F, true) { //

			/**
			 * 
			 */
			private static final long serialVersionUID = -4476957430545177369L;

			// LinkedHashMap自带的判断是否删除最老的元素方法，默认返回false，即不删除老数据
			// 我们要做的就是重写这个方法，当满足一定条件时删除老数据
			// 方法调用链
			//LinkedHashMap.removeEldestEntry ->  LinkedHashMap.void afterNodeInsertion(boolean evict) ->  
			//HashMap.final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict)
			@Override
			protected boolean removeEldestEntry(Map.Entry<K, K> eldest) {
				boolean tooBig = size() > size;
				if (tooBig) {
					eldestKey = eldest.getKey();
				}
				return tooBig;
			}
		};
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