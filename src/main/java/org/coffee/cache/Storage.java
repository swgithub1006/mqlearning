package org.coffee.cache;

/**
 * KV 存储抽象
 */
public interface Storage<K, V> {
	/**
	 * 根据提供的 key 来访问数据
	 * 
	 * @param key
	 *            数据 Key
	 * @return 数据值
	 */
	V get(K key);

	void put(K key, V value);

	String getId();

	V removeByKey(K key);

	void clear();

	int getSize();

}