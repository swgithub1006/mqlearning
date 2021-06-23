package org.coffee.mqlearning.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LRU<K, V> {

	private Node head, tail;
	// 缓存存储上限
	private int capacity;

	private ConcurrentMap<K, Node> nodeMap;

	// private static final float LOAD_FACTOR = 0.75f;

	// private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	

	public LRU(int size) {
		this.capacity = size;
		nodeMap = new ConcurrentHashMap<>(this.capacity);
	}

	public void put(K key, V value) {
		if (key == null) {
			throw new NullPointerException();
		}
		if (value == null) {
			throw new NullPointerException();
		}
		Node node = null;
		if (isEmpty()) {
			node = new Node(key);
			addNodeToTail(node);
			nodeMap.put(key, node);
		} else {
			node = nodeMap.get(key);
			if (node == null) {
				// 如果key不存在，插入key-value
				if (nodeMap.size() >= this.capacity) {
					K oldKey = removeNode(head);
					nodeMap.remove(oldKey);
				}
				node = new Node(key);
				addNodeToTail(node);
			} else {
				// 如果key存在，刷新key-value
				refreshNode(node);
			}
			nodeMap.putIfAbsent(key, node);
		}
	}

	public V get(K key) {
		Node node = nodeMap.get(key);
		if (node == null) {
			return null;
		}
		refreshNode(node);
		return node.value;
	}

	/**
	 * 
	 * 刷新被访问的节点位置
	 * 
	 * @param node
	 *            被访问的节点
	 * 
	 */
	private void refreshNode(Node node) {
		// 如果访问的是尾节点，无需移动节点
		if (node == tail) {
			return;
		}
		// 移除节点
		removeNode(node);
		// 重新插入节点
		addNodeToTail(node);
	}

	/**
	 * 
	 * 删除节点
	 * 
	 * @param node
	 *            要删除的节点
	 * 
	 */

	private K removeNode(Node node) {
		if (node == tail) {
			// 移除尾节点
			tail = tail.pre;
		} else if (node == head) {
			// 移除头节点
			head = head.next;
		} else {
			// 移除中间节点
			node.pre.next = node.next;
			node.next.pre = node.pre;
		}
		return node.getItem();
	}

	/**
	 * 
	 * 尾部插入节点
	 * 
	 * @param node
	 *            要插入的节点
	 * 
	 */
	private void addNodeToTail(Node node) {
		if (node == null) {
			throw new NullPointerException();
		}
		if (isEmpty()) {
			head = tail = node;
		} else {
			tail.next = node;
			node.pre = tail;
			node.next = null;
			tail = node;
		}

	}

	private boolean isEmpty() {
		boolean isEmpty = (head == null && tail == null);
		return isEmpty;
	}

	class Node {
		K item;

		V value;
		// 前一个结点
		Node pre;
		// 后一个结点
		Node next;

		Node(K item) {
			this.item = item;
			pre = next = null;
		}

		K getItem() {
			return this.item;
		}

	}

	public static void main(String[] args) {

	}
}
